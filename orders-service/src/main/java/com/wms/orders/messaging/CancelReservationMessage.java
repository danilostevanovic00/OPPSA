package com.wms.orders.messaging;

/**
 * Message published to cancel a stock reservation.
 */
public class CancelReservationMessage {
    private String shipmentRequestId;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }
}
