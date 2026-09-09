package com.wms.orders.messaging;

import com.wms.orders.config.RabbitMQConfig;
import com.wms.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens for catalog sync, stock reservation and payment events from inventory-service and billing-service.
 */
@Component
public class OrdersConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrdersConsumer.class);

    private final OrderService orderService;

    public OrdersConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.Q_ITEM_CREATED)
    public void handleItemCreated(ItemCreatedEvent event) {
        log.info("[CONSUME] queue={} itemCode={}", RabbitMQConfig.Q_ITEM_CREATED, event.getItemCode());
        orderService.handleItemCreated(event);
    }

    @RabbitListener(queues = RabbitMQConfig.Q_STOCK_REPLENISHED)
    public void handleStockReplenished(StockReplenishedEvent event) {
        log.info("[CONSUME] queue={} warehouse={}", RabbitMQConfig.Q_STOCK_REPLENISHED, event.getWarehouseCode());
        orderService.handleStockReplenished(event);
    }

    @RabbitListener(queues = RabbitMQConfig.Q_STOCK_RESERVED)
    public void handleStockReserved(StockReservedEvent event) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_STOCK_RESERVED, event.getShipmentRequestId());
        orderService.handleStockReserved(event);
    }

    @RabbitListener(queues = RabbitMQConfig.Q_STOCK_RESERVATION_FAILED)
    public void handleStockReservationFailed(StockReservationFailedEvent event) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_STOCK_RESERVATION_FAILED, event.getShipmentRequestId());
        orderService.handleStockReservationFailed(event);
    }

    @RabbitListener(queues = RabbitMQConfig.Q_PAYMENT_CONFIRMED)
    public void handlePaymentConfirmed(PaymentConfirmedMessage message) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_PAYMENT_CONFIRMED, message.getShipmentRequestId());
        orderService.handlePaymentConfirmed(message);
    }
}
