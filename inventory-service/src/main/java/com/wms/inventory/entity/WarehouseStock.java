package com.wms.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Stock level of an item in a specific warehouse.
 */
@Entity
@Table(name = "warehouse_stock", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"warehouse_code", "item_code"})
})
public class WarehouseStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_code", nullable = false, length = 50)
    private String warehouseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", nullable = false)
    private Item item;

    @Column(name = "total_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal totalQuantity = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    public BigDecimal getAvailableQuantity() {
        return totalQuantity.subtract(reservedQuantity);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(BigDecimal totalQuantity) { this.totalQuantity = totalQuantity; }

    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }
}
