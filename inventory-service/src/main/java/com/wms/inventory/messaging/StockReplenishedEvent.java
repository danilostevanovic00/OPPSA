package com.wms.inventory.messaging;

import java.math.BigDecimal;
import java.util.List;

/**
 * Event broadcast when a warehouse's stock is replenished via goods reception.
 */
public class StockReplenishedEvent {
    private String warehouseCode;
    private List<ReplenishedItem> items;

    public static class ReplenishedItem {
        private String itemCode;
        private BigDecimal quantity;
        private BigDecimal purchasePrice;

        public ReplenishedItem() {}

        public ReplenishedItem(String itemCode, BigDecimal quantity, BigDecimal purchasePrice) {
            this.itemCode = itemCode;
            this.quantity = quantity;
            this.purchasePrice = purchasePrice;
        }

        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }

        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

        public BigDecimal getPurchasePrice() { return purchasePrice; }
        public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    }

    public StockReplenishedEvent() {}

    public StockReplenishedEvent(String warehouseCode, List<ReplenishedItem> items) {
        this.warehouseCode = warehouseCode;
        this.items = items;
    }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public List<ReplenishedItem> getItems() { return items; }
    public void setItems(List<ReplenishedItem> items) { this.items = items; }
}
