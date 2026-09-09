package com.wms.inventory.dto;

import java.math.BigDecimal;

/**
 * Response payload representing an item's stock in a specific warehouse.
 */
public class WarehouseStockResponse {
    private String warehouseCode;
    private String itemCode;
    private BigDecimal totalQuantity;
    private BigDecimal reservedQuantity;
    private BigDecimal availableQuantity;

    public WarehouseStockResponse() {}

    public WarehouseStockResponse(String warehouseCode, String itemCode,
                                   BigDecimal totalQuantity, BigDecimal reservedQuantity,
                                   BigDecimal availableQuantity) {
        this.warehouseCode = warehouseCode;
        this.itemCode = itemCode;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public BigDecimal getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(BigDecimal totalQuantity) { this.totalQuantity = totalQuantity; }

    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public BigDecimal getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(BigDecimal availableQuantity) { this.availableQuantity = availableQuantity; }
}
