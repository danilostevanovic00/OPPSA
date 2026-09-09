package com.wms.orders.messaging;

/**
 * Event published when a shipment's payment-ready status is cancelled.
 */
public class ShipmentPaymentReadyCancelledEvent {
    private String shipmentRequestId;

    public ShipmentPaymentReadyCancelledEvent() {}

    public ShipmentPaymentReadyCancelledEvent(String shipmentRequestId) {
        this.shipmentRequestId = shipmentRequestId;
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
