package com.wms.billing.service;

import com.wms.billing.dto.PayShipmentRequest;
import com.wms.billing.entity.Payment;
import com.wms.billing.entity.PaymentReadyShipment;
import com.wms.billing.exception.ConflictException;
import com.wms.billing.messaging.BillingPublisher;
import com.wms.billing.messaging.PaymentConfirmedMessage;
import com.wms.billing.repository.PaymentReadyShipmentRepository;
import com.wms.billing.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles shipment payment processing and payment-readiness tracking.
 */
@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentReadyShipmentRepository paymentReadyShipmentRepository;
    private final BillingPublisher publisher;

    public BillingService(PaymentRepository paymentRepository,
                          PaymentReadyShipmentRepository paymentReadyShipmentRepository,
                          BillingPublisher publisher) {
        this.paymentRepository = paymentRepository;
        this.paymentReadyShipmentRepository = paymentReadyShipmentRepository;
        this.publisher = publisher;
    }

    @Transactional
    public void payShipment(PayShipmentRequest request) {
        String shipmentRequestId = request.getShipmentRequestId();

        if (!paymentReadyShipmentRepository.existsById(shipmentRequestId)) {
            log.warn("[BILLING] Payment rejected - not ready for payment: shipmentRequestId={}",
                    shipmentRequestId);
            throw new ConflictException(
                    "Shipment request is not ready for payment (not reserved yet, "
                    + "already paid, or cancelled/expired): " + shipmentRequestId);
        }

        Payment payment = new Payment();
        payment.setShipmentRequestId(shipmentRequestId);
        payment.setPaymentDate(request.getPaymentDate());
        paymentRepository.save(payment);
        log.info("[BILLING] Payment recorded for shipmentRequestId={}, paymentDate={}",
                shipmentRequestId, request.getPaymentDate());

        paymentReadyShipmentRepository.deleteById(shipmentRequestId);

        publisher.publishPaymentConfirmed(
                new PaymentConfirmedMessage(shipmentRequestId, request.getPaymentDate()));
    }

    @Transactional
    public void markShipmentReadyForPayment(String shipmentRequestId) {
        PaymentReadyShipment ready = new PaymentReadyShipment();
        ready.setShipmentRequestId(shipmentRequestId);
        paymentReadyShipmentRepository.save(ready);
        log.info("[BILLING] Shipment marked ready for payment: shipmentRequestId={}", shipmentRequestId);
    }

    @Transactional
    public void removeShipmentReadyForPayment(String shipmentRequestId) {
        paymentReadyShipmentRepository.deleteById(shipmentRequestId);
        log.info("[BILLING] Shipment payment-readiness cancelled: shipmentRequestId={}", shipmentRequestId);
    }
}

