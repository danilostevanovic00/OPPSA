package com.wms.orders.dto;

import java.math.BigDecimal;

/**
 * Line item nested inside a {@link CreateOrderRequest}.
 */
public class CreateOrderItemRequest {
    private String itemCode;
    private BigDecimal quantity;
    private String unitOfMeasure;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
}
