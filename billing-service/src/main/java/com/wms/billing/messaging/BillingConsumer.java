package com.wms.billing.messaging;

import com.wms.billing.config.RabbitMQConfig;
import com.wms.billing.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens for shipment payment-readiness events from orders-service.
 */
@Component
public class BillingConsumer {

    private static final Logger log = LoggerFactory.getLogger(BillingConsumer.class);

    private final BillingService billingService;

    public BillingConsumer(BillingService billingService) {
        this.billingService = billingService;
    }

    @RabbitListener(queues = RabbitMQConfig.Q_SHIPMENT_PAYMENT_READY)
    public void handleShipmentPaymentReady(ShipmentPaymentReadyEvent event) {
        log.info("[CONSUME] queue={} shipmentRequestId={}",
                RabbitMQConfig.Q_SHIPMENT_PAYMENT_READY, event.getShipmentRequestId());
        billingService.markShipmentReadyForPayment(event.getShipmentRequestId());
    }

    @RabbitListener(queues = RabbitMQConfig.Q_SHIPMENT_PAYMENT_READY_CANCELLED)
    public void handleShipmentPaymentReadyCancelled(ShipmentPaymentReadyCancelledEvent event) {
        log.info("[CONSUME] queue={} shipmentRequestId={}",
                RabbitMQConfig.Q_SHIPMENT_PAYMENT_READY_CANCELLED, event.getShipmentRequestId());
        billingService.removeShipmentReadyForPayment(event.getShipmentRequestId());
    }
}
