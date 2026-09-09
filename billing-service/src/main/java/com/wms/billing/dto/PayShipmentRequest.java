package com.wms.billing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Request payload for paying a shipment.
 */
public class PayShipmentRequest {
    private String shipmentRequestId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
