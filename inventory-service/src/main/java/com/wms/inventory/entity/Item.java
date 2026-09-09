package com.wms.inventory.entity;

import jakarta.persistence.*;

/**
 * An inventory item that can be stocked and sold.
 */
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "item_code", nullable = false, unique = true, length = 50)
    private String itemCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
