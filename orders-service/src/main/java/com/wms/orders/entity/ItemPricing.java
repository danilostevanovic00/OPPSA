package com.wms.orders.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Local copy of item pricing data, kept in sync with inventory-service via events.
 */
@Entity
@Table(name = "item_pricing")
public class ItemPricing {

    @Id
    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;

    @Column(name = "average_purchase_price", precision = 15, scale = 4)
    private BigDecimal averagePurchasePrice;

    @Column(name = "custom_margin_percent", precision = 5, scale = 2)
    private BigDecimal customMarginPercent;

    @Column(name = "total_quantity_received", precision = 15, scale = 3)
    private BigDecimal totalQuantityReceived = BigDecimal.ZERO;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getAveragePurchasePrice() { return averagePurchasePrice; }
    public void setAveragePurchasePrice(BigDecimal averagePurchasePrice) { this.averagePurchasePrice = averagePurchasePrice; }

    public BigDecimal getCustomMarginPercent() { return customMarginPercent; }
    public void setCustomMarginPercent(BigDecimal customMarginPercent) { this.customMarginPercent = customMarginPercent; }

    public BigDecimal getTotalQuantityReceived() { return totalQuantityReceived; }
    public void setTotalQuantityReceived(BigDecimal totalQuantityReceived) { this.totalQuantityReceived = totalQuantityReceived; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
