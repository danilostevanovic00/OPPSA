package com.wms.orders.dto;

import java.math.BigDecimal;

/**
 * Line item nested inside an {@link OrderResponse}.
 */
public class OrderItemResponse {
    private String itemCode;
    private String itemName;
    private BigDecimal requestedQuantity;
    private String unitOfMeasure;
    private BigDecimal sellingPricePerUnit;
    private BigDecimal lineTotal;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public BigDecimal getRequestedQuantity() { return requestedQuantity; }
    public void setRequestedQuantity(BigDecimal requestedQuantity) { this.requestedQuantity = requestedQuantity; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getSellingPricePerUnit() { return sellingPricePerUnit; }
    public void setSellingPricePerUnit(BigDecimal sellingPricePerUnit) { this.sellingPricePerUnit = sellingPricePerUnit; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}
