package com.wms.orders.messaging;

import java.math.BigDecimal;
import java.util.List;

/**
 * Message published when a shipment's goods have been sold and paid for.
 */
public class GoodsSoldMessage {
    private String shipmentRequestId;
    private List<SoldItem> items;

    public static class SoldItem {
        private String itemCode;
        private BigDecimal quantity;

        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }

        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public List<SoldItem> getItems() { return items; }
    public void setItems(List<SoldItem> items) { this.items = items; }
}
