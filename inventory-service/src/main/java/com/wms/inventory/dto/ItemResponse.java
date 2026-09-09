package com.wms.inventory.dto;

/**
 * Response payload representing an inventory item.
 */
public class ItemResponse {
    private String itemCode;
    private String name;
    private String unitOfMeasure;
    private boolean active;

    public ItemResponse() {}

    public ItemResponse(String itemCode, String name, String unitOfMeasure, boolean active) {
        this.itemCode = itemCode;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.active = active;
    }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
