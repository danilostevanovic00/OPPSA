package com.wms.inventory.messaging;

import java.math.BigDecimal;
import java.util.List;

/**
 * Event broadcast when stock is successfully reserved for a shipment request.
 */
public class StockReservedEvent {
    private String shipmentRequestId;
    private List<ReservationDetail> reservations;

    public static class ReservationDetail {
        private String warehouseCode;
        private String itemCode;
        private BigDecimal quantity;

        public ReservationDetail() {}

        public ReservationDetail(String warehouseCode, String itemCode, BigDecimal quantity) {
            this.warehouseCode = warehouseCode;
            this.itemCode = itemCode;
            this.quantity = quantity;
        }

        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }

        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    }

    public StockReservedEvent() {}

    public StockReservedEvent(String shipmentRequestId, List<ReservationDetail> reservations) {
        this.shipmentRequestId = shipmentRequestId;
        this.reservations = reservations;
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public List<ReservationDetail> getReservations() { return reservations; }
    public void setReservations(List<ReservationDetail> reservations) { this.reservations = reservations; }
}
