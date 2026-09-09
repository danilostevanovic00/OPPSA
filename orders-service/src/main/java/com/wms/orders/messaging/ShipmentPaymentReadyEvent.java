package com.wms.orders.messaging;

/**
 * Event published when a shipment request becomes ready for payment.
 */
public class ShipmentPaymentReadyEvent {
    private String shipmentRequestId;

    public ShipmentPaymentReadyEvent() {}

    public ShipmentPaymentReadyEvent(String shipmentRequestId) {
        this.shipmentRequestId = shipmentRequestId;
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
