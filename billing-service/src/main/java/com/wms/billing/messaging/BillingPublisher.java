package com.wms.billing.messaging;

import com.wms.billing.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes billing-related events to RabbitMQ.
 */
@Component
public class BillingPublisher {

    private static final Logger log = LoggerFactory.getLogger(BillingPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public BillingPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPaymentConfirmed(PaymentConfirmedMessage message) {
        log.info("[PUBLISH] routingKey={} shipmentRequestId={}",
                RabbitMQConfig.RK_PAYMENT_CONFIRMED, message.getShipmentRequestId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE, RabbitMQConfig.RK_PAYMENT_CONFIRMED, message);
    }
}
