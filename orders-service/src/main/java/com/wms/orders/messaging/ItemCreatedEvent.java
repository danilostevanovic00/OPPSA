package com.wms.orders.messaging;

/**
 * Event received when a new inventory item is created.
 */
public class ItemCreatedEvent {
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
