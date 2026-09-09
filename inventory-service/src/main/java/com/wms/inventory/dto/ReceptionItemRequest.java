package com.wms.inventory.dto;

import java.math.BigDecimal;

/**
 * Line item nested inside a {@link ReceptionRequest}.
 */
public class ReceptionItemRequest {
    private String itemCode;
    private BigDecimal quantity;
    private String unitOfMeasure;
    private BigDecimal purchasePrice;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
}
