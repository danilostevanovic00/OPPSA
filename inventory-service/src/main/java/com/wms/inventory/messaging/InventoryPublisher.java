package com.wms.inventory.messaging;

import com.wms.inventory.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes inventory-related events to RabbitMQ.
 */
@Component
public class InventoryPublisher {

    private static final Logger log = LoggerFactory.getLogger(InventoryPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public InventoryPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishItemCreated(ItemCreatedEvent event) {
        log.info("[PUBLISH] routingKey={} itemCode={}", RabbitMQConfig.RK_ITEM_CREATED, event.getItemCode());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_ITEM_CREATED, event);
    }

    public void publishStockReplenished(StockReplenishedEvent event) {
        log.info("[PUBLISH] routingKey={} warehouse={}", RabbitMQConfig.RK_STOCK_REPLENISHED, event.getWarehouseCode());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_STOCK_REPLENISHED, event);
    }

    public void publishStockReserved(StockReservedEvent event) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_STOCK_RESERVED, event.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_STOCK_RESERVED, event);
    }

    public void publishStockReservationFailed(StockReservationFailedEvent event) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={} reason={}",
                RabbitMQConfig.RK_STOCK_RESERVATION_FAILED, event.getShipmentRequestId(), event.getReason());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_STOCK_RESERVATION_FAILED, event);
    }
}
