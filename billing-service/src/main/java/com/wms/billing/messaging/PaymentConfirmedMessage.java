package com.wms.billing.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Event published when a shipment payment is confirmed.
 */
public class PaymentConfirmedMessage {
    private String shipmentRequestId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    public PaymentConfirmedMessage() {}

    public PaymentConfirmedMessage(String shipmentRequestId, LocalDate paymentDate) {
        this.shipmentRequestId = shipmentRequestId;
        this.paymentDate = paymentDate;
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
