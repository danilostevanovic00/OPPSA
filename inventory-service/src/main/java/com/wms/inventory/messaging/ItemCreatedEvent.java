package com.wms.inventory.messaging;

/**
 * Event broadcast when a new inventory item is created.
 */
public class ItemCreatedEvent {
    private String itemCode;
    private String name;
    private String unitOfMeasure;

    public ItemCreatedEvent() {}

    public ItemCreatedEvent(String itemCode, String name, String unitOfMeasure) {
        this.itemCode = itemCode;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
}
