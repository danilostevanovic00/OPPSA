package com.wms.orders.messaging;

import java.math.BigDecimal;
import java.util.List;

/**
 * Message published to request stock reservation for a shipment request.
 */
public class ReserveStockMessage {
    private String shipmentRequestId;
    private List<StockItem> items;

    public static class StockItem {
        private String itemCode;
        private BigDecimal quantity;

        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }

        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public List<StockItem> getItems() { return items; }
    public void setItems(List<StockItem> items) { this.items = items; }
}
