package com.wms.inventory.service;

import com.wms.inventory.dto.*;
import com.wms.inventory.entity.*;
import com.wms.inventory.exception.ConflictException;
import com.wms.inventory.exception.ResourceNotFoundException;
import com.wms.inventory.messaging.*;
import com.wms.inventory.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles item catalog, warehouse stock and stock reservation business logic.
 */
@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final ItemRepository itemRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final GoodsReceptionRepository goodsReceptionRepository;
    private final StockReservationRepository stockReservationRepository;
    private final DispatchNoteRepository dispatchNoteRepository;
    private final InventoryPublisher publisher;

    public InventoryService(ItemRepository itemRepository,
                            WarehouseStockRepository warehouseStockRepository,
                            GoodsReceptionRepository goodsReceptionRepository,
                            StockReservationRepository stockReservationRepository,
                            DispatchNoteRepository dispatchNoteRepository,
                            InventoryPublisher publisher) {
        this.itemRepository = itemRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.goodsReceptionRepository = goodsReceptionRepository;
        this.stockReservationRepository = stockReservationRepository;
        this.dispatchNoteRepository = dispatchNoteRepository;
        this.publisher = publisher;
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(item -> new ItemResponse(
                        item.getItemCode(), item.getName(),
                        item.getUnitOfMeasure(), item.isActive()))
                .collect(Collectors.toList());
    }

    public List<WarehouseStockResponse> getItemState(String itemCode) {
        Item item = itemRepository.findById(itemCode)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemCode));
        return warehouseStockRepository.findByItem(item).stream()
                .map(ws -> new WarehouseStockResponse(
                        ws.getWarehouseCode(), itemCode,
                        ws.getTotalQuantity(), ws.getReservedQuantity(),
                        ws.getAvailableQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void newItem(NewItemRequest request) {
        if (itemRepository.existsById(request.getItemCode())) {
            throw new ConflictException("Item already exists: " + request.getItemCode());
        }
        Item item = new Item();
        item.setItemCode(request.getItemCode());
        item.setName(request.getName());
        item.setUnitOfMeasure(request.getUnitOfMeasure());
        item.setActive(true);
        itemRepository.save(item);
        log.info("[INVENTORY] New item created: code={}, name={}, unit={}",
                request.getItemCode(), request.getName(), request.getUnitOfMeasure());

        publisher.publishItemCreated(new ItemCreatedEvent(
                request.getItemCode(), request.getName(), request.getUnitOfMeasure()));
    }

    @Transactional
    public void receptionOfGoods(ReceptionRequest request) {
        GoodsReception reception = new GoodsReception();
        reception.setReceptionDate(request.getDate());
        reception.setSupplierCode(request.getSupplierCode());
        reception.setWarehouseCode(request.getWarehouseCode());

        List<StockReplenishedEvent.ReplenishedItem> replenishedItems = new ArrayList<>();

        for (ReceptionItemRequest itemReq : request.getItems()) {
            Item item = itemRepository.findById(itemReq.getItemCode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Item not found: " + itemReq.getItemCode()));

            GoodsReceptionItem receptionItem = new GoodsReceptionItem();
            receptionItem.setReception(reception);
            receptionItem.setItem(item);
            receptionItem.setQuantity(itemReq.getQuantity());
            receptionItem.setUnitOfMeasure(itemReq.getUnitOfMeasure());
            receptionItem.setPurchasePrice(itemReq.getPurchasePrice());
            reception.getItems().add(receptionItem);

            WarehouseStock stock = warehouseStockRepository
                    .findByWarehouseCodeAndItem(request.getWarehouseCode(), item)
                    .orElseGet(() -> {
                        WarehouseStock newStock = new WarehouseStock();
                        newStock.setWarehouseCode(request.getWarehouseCode());
                        newStock.setItem(item);
                        return newStock;
                    });
            stock.setTotalQuantity(stock.getTotalQuantity().add(itemReq.getQuantity()));
            warehouseStockRepository.save(stock);

            replenishedItems.add(new StockReplenishedEvent.ReplenishedItem(
                    itemReq.getItemCode(), itemReq.getQuantity(), itemReq.getPurchasePrice()));
        }

        goodsReceptionRepository.save(reception);
        log.info("[INVENTORY] Goods received: warehouse={}, supplier={}, {} item line(s)",
                request.getWarehouseCode(), request.getSupplierCode(), replenishedItems.size());
        publisher.publishStockReplenished(
                new StockReplenishedEvent(request.getWarehouseCode(), replenishedItems));
    }

    /**
     * Reserves stock across warehouses using a greedy first-available strategy.
     */
    @Transactional
    public void reserveStock(ReserveStockMessage message) {
        String shipmentRequestId = message.getShipmentRequestId();
        log.info("[SAGA] Reservation requested for shipmentRequestId={}, {} item line(s)",
                shipmentRequestId, message.getItems().size());

        Map<Long, BigDecimal> pendingReservations = new LinkedHashMap<>();
        List<StockReservation> newReservations = new ArrayList<>();
        List<StockReservedEvent.ReservationDetail> details = new ArrayList<>();

        for (ReserveStockMessage.StockItem stockItem : message.getItems()) {
            Item item = itemRepository.findById(stockItem.getItemCode()).orElse(null);
            if (item == null) {
                log.warn("[SAGA] Reservation FAILED for shipmentRequestId={} - item not found: {}",
                        shipmentRequestId, stockItem.getItemCode());
                publisher.publishStockReservationFailed(new StockReservationFailedEvent(
                        shipmentRequestId, "Item not found: " + stockItem.getItemCode()));
                return;
            }

            List<WarehouseStock> stocks = warehouseStockRepository.findByItem(item);
            BigDecimal remaining = stockItem.getQuantity();

            for (WarehouseStock ws : stocks) {
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal alreadyPending = pendingReservations.getOrDefault(ws.getId(), BigDecimal.ZERO);
                BigDecimal trueAvailable = ws.getAvailableQuantity().subtract(alreadyPending);
                if (trueAvailable.compareTo(BigDecimal.ZERO) <= 0) continue;

                BigDecimal toReserve = trueAvailable.min(remaining);
                pendingReservations.merge(ws.getId(), toReserve, BigDecimal::add);
                remaining = remaining.subtract(toReserve);

                StockReservation reservation = new StockReservation();
                reservation.setShipmentRequestId(shipmentRequestId);
                reservation.setWarehouseCode(ws.getWarehouseCode());
                reservation.setItem(item);
                reservation.setReservedQuantity(toReserve);
                newReservations.add(reservation);

                details.add(new StockReservedEvent.ReservationDetail(
                        ws.getWarehouseCode(), stockItem.getItemCode(), toReserve));
            }

            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                log.warn("[SAGA] Reservation FAILED for shipmentRequestId={} - insufficient stock for item: {}",
                        shipmentRequestId, stockItem.getItemCode());
                publisher.publishStockReservationFailed(new StockReservationFailedEvent(
                        shipmentRequestId, "Insufficient stock for item: " + stockItem.getItemCode()));
                return;
            }
        }

        for (Map.Entry<Long, BigDecimal> entry : pendingReservations.entrySet()) {
            WarehouseStock ws = warehouseStockRepository.findById(entry.getKey()).orElseThrow();
            ws.setReservedQuantity(ws.getReservedQuantity().add(entry.getValue()));
            warehouseStockRepository.save(ws);
        }
        stockReservationRepository.saveAll(newReservations);

        log.info("[SAGA] Reservation SUCCEEDED for shipmentRequestId={} across {} warehouse line(s)",
                shipmentRequestId, details.size());
        publisher.publishStockReserved(new StockReservedEvent(shipmentRequestId, details));
    }

    @Transactional
    public void cancelReservation(String shipmentRequestId) {
        log.info("[SAGA] Cancelling reservation for shipmentRequestId={}", shipmentRequestId);
        List<StockReservation> reservations =
                stockReservationRepository.findByShipmentRequestId(shipmentRequestId);

        for (StockReservation reservation : reservations) {
            WarehouseStock stock = warehouseStockRepository
                    .findByWarehouseCodeAndItem(reservation.getWarehouseCode(), reservation.getItem())
                    .orElseThrow();
            stock.setReservedQuantity(
                    stock.getReservedQuantity().subtract(reservation.getReservedQuantity()));
            warehouseStockRepository.save(stock);
        }
        stockReservationRepository.deleteAll(reservations);
        log.info("[SAGA] Reservation cancelled for shipmentRequestId={}, {} line(s) released",
                shipmentRequestId, reservations.size());
    }

    /**
     * Creates dispatch notes from stored reservations and removes dispatched quantities from stock.
     */
    @Transactional
    public void processGoodsSold(GoodsSoldMessage message) {
        String shipmentRequestId = message.getShipmentRequestId();
        log.info("[INVENTORY] Goods sold received for shipmentRequestId={}", shipmentRequestId);
        List<StockReservation> reservations =
                stockReservationRepository.findByShipmentRequestId(shipmentRequestId);

        for (StockReservation reservation : reservations) {
            WarehouseStock stock = warehouseStockRepository
                    .findByWarehouseCodeAndItem(reservation.getWarehouseCode(), reservation.getItem())
                    .orElseThrow();
            stock.setTotalQuantity(
                    stock.getTotalQuantity().subtract(reservation.getReservedQuantity()));
            stock.setReservedQuantity(
                    stock.getReservedQuantity().subtract(reservation.getReservedQuantity()));
            warehouseStockRepository.save(stock);

            DispatchNote note = new DispatchNote();
            note.setShipmentRequestId(shipmentRequestId);
            note.setWarehouseCode(reservation.getWarehouseCode());
            note.setItem(reservation.getItem());
            note.setQuantity(reservation.getReservedQuantity());
            dispatchNoteRepository.save(note);
            log.info("[INVENTORY] DispatchNote created: shipmentRequestId={}, warehouse={}, item={}, qty={}",
                    shipmentRequestId, reservation.getWarehouseCode(),
                    reservation.getItem().getItemCode(), reservation.getReservedQuantity());
        }
        stockReservationRepository.deleteAll(reservations);
    }
}
