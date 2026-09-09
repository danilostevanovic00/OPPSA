package com.wms.inventory.messaging;

/**
 * Message received to cancel a previously reserved stock quantity.
 */
public class CancelReservationMessage {
    private String shipmentRequestId;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
