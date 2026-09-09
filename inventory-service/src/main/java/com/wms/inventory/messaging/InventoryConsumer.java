package com.wms.inventory.messaging;

import com.wms.inventory.config.RabbitMQConfig;
import com.wms.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens for stock reservation, cancellation and goods-sold messages from orders-service.
 */
@Component
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RabbitListener(queues = RabbitMQConfig.Q_RESERVE_STOCK)
    public void handleReserveStock(ReserveStockMessage message) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_RESERVE_STOCK, message.getShipmentRequestId());
        inventoryService.reserveStock(message);
    }

    @RabbitListener(queues = RabbitMQConfig.Q_CANCEL_RESERVATION)
    public void handleCancelReservation(CancelReservationMessage message) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_CANCEL_RESERVATION, message.getShipmentRequestId());
        inventoryService.cancelReservation(message.getShipmentRequestId());
    }

    @RabbitListener(queues = RabbitMQConfig.Q_GOODS_SOLD)
    public void handleGoodsSold(GoodsSoldMessage message) {
        log.info("[CONSUME] queue={} shipmentRequestId={}", RabbitMQConfig.Q_GOODS_SOLD, message.getShipmentRequestId());
        inventoryService.processGoodsSold(message);
    }
}
