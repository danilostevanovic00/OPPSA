package com.wms.inventory.dto;

/**
 * Request payload for creating a new inventory item.
 */
public class NewItemRequest {
    private String itemCode;
    private String name;
    private String unitOfMeasure;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
}
