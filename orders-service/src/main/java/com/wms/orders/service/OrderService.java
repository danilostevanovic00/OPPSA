package com.wms.orders.service;

import com.wms.orders.dto.*;
import com.wms.orders.entity.*;
import com.wms.orders.exception.ConflictException;
import com.wms.orders.exception.ResourceNotFoundException;
import com.wms.orders.messaging.*;
import com.wms.orders.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implements the order lifecycle and the stock-reservation/payment saga with inventory-service and billing-service.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final BigDecimal DEFAULT_MARGIN = new BigDecimal("15");

    private final OrderRepository orderRepository;
    private final ItemPricingRepository itemPricingRepository;
    private final ShipmentRequestRepository shipmentRequestRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrdersPublisher publisher;

    @Value("${orders.payment-deadline-days:3}")
    private int paymentDeadlineDays;

    public OrderService(OrderRepository orderRepository,
                        ItemPricingRepository itemPricingRepository,
                        ShipmentRequestRepository shipmentRequestRepository,
                        InvoiceRepository invoiceRepository,
                        OrdersPublisher publisher) {
        this.orderRepository = orderRepository;
        this.itemPricingRepository = itemPricingRepository;
        this.shipmentRequestRepository = shipmentRequestRepository;
        this.invoiceRepository = invoiceRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setCreationDate(LocalDate.now());
        order.setCustomerCode(request.getCustomerCode());
        order.setCustomerName(request.getCustomerName());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStatus(OrderStatus.CREATED);

        for (CreateOrderItemRequest itemReq : request.getItems()) {
            ItemPricing pricing = itemPricingRepository.findById(itemReq.getItemCode()).orElse(null);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItemCode(itemReq.getItemCode());
            orderItem.setItemName(pricing != null ? pricing.getItemName() : itemReq.getItemCode());
            orderItem.setRequestedQuantity(itemReq.getQuantity());
            orderItem.setUnitOfMeasure(itemReq.getUnitOfMeasure());
            orderItem.setSellingPricePerUnit(calculateSellingPrice(pricing));
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
        log.info("[ORDER] Created order id={} for customer={}, {} item line(s)",
                order.getId(), order.getCustomerCode(), order.getItems().size());
        return toOrderResponse(order);
    }

    @Transactional
    public ShipmentRequestResponse confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new ConflictException("Order cannot be confirmed in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CONFIRMED);

        BigDecimal total = order.getItems().stream()
                .map(i -> i.getSellingPricePerUnit().multiply(i.getRequestedQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        ShipmentRequest sr = new ShipmentRequest();
        sr.setId(UUID.randomUUID().toString());
        sr.setOrder(order);
        sr.setTotalAmount(total);
        sr.setPaymentDeadline(LocalDate.now().plusDays(paymentDeadlineDays));
        sr.setStatus(ShipmentRequestStatus.PENDING);

        for (OrderItem oi : order.getItems()) {
            ShipmentRequestItem sri = new ShipmentRequestItem();
            sri.setShipmentRequest(sr);
            sri.setItemCode(oi.getItemCode());
            sri.setItemName(oi.getItemName());
            sri.setQuantity(oi.getRequestedQuantity());
            sri.setUnitOfMeasure(oi.getUnitOfMeasure());
            sri.setUnitPrice(oi.getSellingPricePerUnit());
            sr.getItems().add(sri);
        }

        orderRepository.save(order);
        shipmentRequestRepository.save(sr);
        log.info("[SAGA] Step 1: ShipmentRequest id={} created for orderId={}, totalAmount={}, deadline={}",
                sr.getId(), orderId, total, sr.getPaymentDeadline());

        List<ReserveStockMessage.StockItem> stockItems = order.getItems().stream()
                .map(oi -> {
                    ReserveStockMessage.StockItem si = new ReserveStockMessage.StockItem();
                    si.setItemCode(oi.getItemCode());
                    si.setQuantity(oi.getRequestedQuantity());
                    return si;
                })
                .collect(Collectors.toList());

        ReserveStockMessage msg = new ReserveStockMessage();
        msg.setShipmentRequestId(sr.getId());
        msg.setItems(stockItems);
        publisher.publishReserveStock(msg);

        return toShipmentRequestResponse(sr);
    }


    @Transactional
    public void setCustomMargin(String itemCode, BigDecimal marginPercent) {
        ItemPricing pricing = itemPricingRepository.findById(itemCode)
                .orElseGet(() -> {
                    ItemPricing p = new ItemPricing();
                    p.setItemCode(itemCode);
                    p.setTotalQuantityReceived(BigDecimal.ZERO);
                    return p;
                });
        pricing.setCustomMarginPercent(marginPercent);
        itemPricingRepository.save(pricing);
    }


    @Transactional
    public void handleStockReserved(StockReservedEvent event) {
        ShipmentRequest sr = shipmentRequestRepository.findById(event.getShipmentRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ShipmentRequest not found: " + event.getShipmentRequestId()));
        sr.setStatus(ShipmentRequestStatus.PAYMENT_READY);
        shipmentRequestRepository.save(sr);
        log.info("[SAGA] Step 2: ShipmentRequest id={} -> PAYMENT_READY", sr.getId());

        publisher.publishShipmentPaymentReady(new ShipmentPaymentReadyEvent(sr.getId()));
    }

    @Transactional
    public void handleStockReservationFailed(StockReservationFailedEvent event) {
        shipmentRequestRepository.findById(event.getShipmentRequestId()).ifPresent(sr -> {
            sr.setStatus(ShipmentRequestStatus.CANCELLED);
            shipmentRequestRepository.save(sr);
            log.warn("[SAGA] Compensation: ShipmentRequest id={} -> CANCELLED, reason={}",
                    sr.getId(), event.getReason());
        });
    }


    @Transactional
    public void handlePaymentConfirmed(PaymentConfirmedMessage message) {
        ShipmentRequest sr = shipmentRequestRepository.findById(message.getShipmentRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ShipmentRequest not found: " + message.getShipmentRequestId()));

        if (sr.getStatus() != ShipmentRequestStatus.PAYMENT_READY) {
            throw new ConflictException(
                    "ShipmentRequest not in PAYMENT_READY status: " + sr.getStatus());
        }

        sr.setStatus(ShipmentRequestStatus.COMPLETED);

        Invoice invoice = new Invoice();
        invoice.setShipmentRequest(sr);
        invoice.setPaymentDate(message.getPaymentDate());
        invoice.setTotalAmount(sr.getTotalAmount());

        for (ShipmentRequestItem sri : sr.getItems()) {
            InvoiceItem ii = new InvoiceItem();
            ii.setInvoice(invoice);
            ii.setItemCode(sri.getItemCode());
            ii.setItemName(sri.getItemName());
            ii.setQuantity(sri.getQuantity());
            ii.setUnitOfMeasure(sri.getUnitOfMeasure());
            ii.setUnitPrice(sri.getUnitPrice());
            ii.setTotalPrice(sri.getUnitPrice().multiply(sri.getQuantity())
                    .setScale(2, RoundingMode.HALF_UP));
            invoice.getItems().add(ii);
        }

        invoiceRepository.save(invoice);
        shipmentRequestRepository.save(sr);
        log.info("[ORDER] ShipmentRequest id={} -> COMPLETED, Invoice created with totalAmount={}",
                sr.getId(), invoice.getTotalAmount());

        List<GoodsSoldMessage.SoldItem> soldItems = sr.getItems().stream()
                .map(sri -> {
                    GoodsSoldMessage.SoldItem si = new GoodsSoldMessage.SoldItem();
                    si.setItemCode(sri.getItemCode());
                    si.setQuantity(sri.getQuantity());
                    return si;
                })
                .collect(Collectors.toList());

        GoodsSoldMessage soldMsg = new GoodsSoldMessage();
        soldMsg.setShipmentRequestId(sr.getId());
        soldMsg.setItems(soldItems);
        publisher.publishGoodsSold(soldMsg);
    }

    @Scheduled(cron = "${orders.expiry-check.cron:0 0 8 * * *}")
    @Transactional
    public void cancelExpiredShipmentRequests() {
        log.info("[SCHEDULED] Running daily expiry check for unpaid ShipmentRequests");
        List<ShipmentRequest> expired = shipmentRequestRepository
                .findByStatusAndPaymentDeadlineBefore(
                        ShipmentRequestStatus.PAYMENT_READY, LocalDate.now());

        for (ShipmentRequest sr : expired) {
            sr.setStatus(ShipmentRequestStatus.EXPIRED);
            shipmentRequestRepository.save(sr);
            log.warn("[SCHEDULED] ShipmentRequest id={} expired (deadline={}), sending CancelReservation",
                    sr.getId(), sr.getPaymentDeadline());

            CancelReservationMessage msg = new CancelReservationMessage();
            msg.setShipmentRequestId(sr.getId());
            publisher.publishCancelReservation(msg);

            publisher.publishShipmentPaymentReadyCancelled(new ShipmentPaymentReadyCancelledEvent(sr.getId()));
        }
        log.info("[SCHEDULED] Expiry check completed, {} ShipmentRequest(s) expired", expired.size());
    }

    @Transactional
    public void handleItemCreated(ItemCreatedEvent event) {
        ItemPricing pricing = itemPricingRepository.findById(event.getItemCode())
                .orElseGet(() -> {
                    ItemPricing p = new ItemPricing();
                    p.setItemCode(event.getItemCode());
                    p.setTotalQuantityReceived(BigDecimal.ZERO);
                    return p;
                });
        pricing.setItemName(event.getName());
        pricing.setUnitOfMeasure(event.getUnitOfMeasure());
        pricing.setLastUpdated(LocalDateTime.now());
        itemPricingRepository.save(pricing);
        log.info("[ORDER] Local catalog synced: itemCode={}, name={}", event.getItemCode(), event.getName());
    }

    @Transactional
    public void handleStockReplenished(StockReplenishedEvent event) {
        for (StockReplenishedEvent.ReplenishedItem repItem : event.getItems()) {
            ItemPricing pricing = itemPricingRepository.findById(repItem.getItemCode())
                    .orElseGet(() -> {
                        ItemPricing p = new ItemPricing();
                        p.setItemCode(repItem.getItemCode());
                        p.setTotalQuantityReceived(BigDecimal.ZERO);
                        return p;
                    });

            BigDecimal oldQty = pricing.getTotalQuantityReceived() != null
                    ? pricing.getTotalQuantityReceived() : BigDecimal.ZERO;
            BigDecimal oldAvg = pricing.getAveragePurchasePrice() != null
                    ? pricing.getAveragePurchasePrice() : BigDecimal.ZERO;
            BigDecimal newQty = repItem.getQuantity();
            BigDecimal totalQty = oldQty.add(newQty);

            BigDecimal newAvg = oldAvg.multiply(oldQty)
                    .add(repItem.getPurchasePrice().multiply(newQty))
                    .divide(totalQty, 4, RoundingMode.HALF_UP);

            pricing.setAveragePurchasePrice(newAvg);
            pricing.setTotalQuantityReceived(totalQty);
            pricing.setLastUpdated(LocalDateTime.now());
            itemPricingRepository.save(pricing);
            log.info("[ORDER] Purchase price updated: itemCode={}, newAveragePrice={}, sellingPrice={}",
                    repItem.getItemCode(), newAvg, calculateSellingPrice(pricing));
        }
    }

    private BigDecimal calculateSellingPrice(ItemPricing pricing) {
        if (pricing == null || pricing.getAveragePurchasePrice() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal margin = pricing.getCustomMarginPercent() != null
                ? pricing.getCustomMarginPercent()
                : DEFAULT_MARGIN;
        return pricing.getAveragePurchasePrice()
                .multiply(BigDecimal.ONE.add(margin.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
                .setScale(4, RoundingMode.HALF_UP);
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCreationDate(order.getCreationDate());
        response.setCustomerCode(order.getCustomerCode());
        response.setCustomerName(order.getCustomerName());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setStatus(order.getStatus().name());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem oi : order.getItems()) {
            OrderItemResponse ir = new OrderItemResponse();
            ir.setItemCode(oi.getItemCode());
            ir.setItemName(oi.getItemName());
            ir.setRequestedQuantity(oi.getRequestedQuantity());
            ir.setUnitOfMeasure(oi.getUnitOfMeasure());
            ir.setSellingPricePerUnit(oi.getSellingPricePerUnit());
            BigDecimal lineTotal = oi.getSellingPricePerUnit()
                    .multiply(oi.getRequestedQuantity()).setScale(2, RoundingMode.HALF_UP);
            ir.setLineTotal(lineTotal);
            total = total.add(lineTotal);
            itemResponses.add(ir);
        }
        response.setItems(itemResponses);
        response.setTotalAmount(total);
        return response;
    }

    private ShipmentRequestResponse toShipmentRequestResponse(ShipmentRequest sr) {
        ShipmentRequestResponse response = new ShipmentRequestResponse();
        response.setId(sr.getId());
        response.setOrderId(sr.getOrder().getId());
        response.setTotalAmount(sr.getTotalAmount());
        response.setPaymentDeadline(sr.getPaymentDeadline());
        response.setStatus(sr.getStatus().name());
        return response;
    }
}
