package com.wms.orders.messaging;

import com.wms.orders.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes order and shipment-related events to RabbitMQ.
 */
@Component
public class OrdersPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrdersPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public OrdersPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReserveStock(ReserveStockMessage message) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_RESERVE_STOCK, message.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_RESERVE_STOCK, message);
    }

    public void publishCancelReservation(CancelReservationMessage message) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_CANCEL_RESERVATION, message.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_CANCEL_RESERVATION, message);
    }

    public void publishGoodsSold(GoodsSoldMessage message) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_GOODS_SOLD, message.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_GOODS_SOLD, message);
    }

    public void publishShipmentPaymentReady(ShipmentPaymentReadyEvent event) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_SHIPMENT_PAYMENT_READY, event.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_SHIPMENT_PAYMENT_READY, event);
    }

    public void publishShipmentPaymentReadyCancelled(ShipmentPaymentReadyCancelledEvent event) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}", RabbitMQConfig.RK_SHIPMENT_PAYMENT_READY_CANCELLED, event.getShipmentRequestId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_SHIPMENT_PAYMENT_READY_CANCELLED, event);
    }
}
