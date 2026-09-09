package com.wms.orders.messaging;

/**
 * Event received when a stock reservation request fails.
 */
public class StockReservationFailedEvent {
    private String shipmentRequestId;
    private String reason;

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
