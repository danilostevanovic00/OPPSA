package com.wms.billing.messaging;

/**
 * Event received when a shipment's payment-ready status is cancelled.
 */
public class ShipmentPaymentReadyCancelledEvent {
    private String shipmentRequestId;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
