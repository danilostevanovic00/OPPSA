package com.wms.billing.messaging;

/**
 * Event received when a shipment request becomes ready for payment.
 */
public class ShipmentPaymentReadyEvent {
    private String shipmentRequestId;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
