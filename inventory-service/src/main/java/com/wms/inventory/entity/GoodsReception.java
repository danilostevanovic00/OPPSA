package com.wms.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Header of a goods reception document from a supplier; line items live in {@link GoodsReceptionItem}.
 */
@Entity
@Table(name = "goods_receptions")
public class GoodsReception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reception_date", nullable = false)
    private LocalDate receptionDate;

    @Column(name = "supplier_code", nullable = false, length = 50)
    private String supplierCode;

    @Column(name = "warehouse_code", nullable = false, length = 50)
    private String warehouseCode;

    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsReceptionItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getReceptionDate() { return receptionDate; }
    public void setReceptionDate(LocalDate receptionDate) { this.receptionDate = receptionDate; }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public List<GoodsReceptionItem> getItems() { return items; }
    public void setItems(List<GoodsReceptionItem> items) { this.items = items; }
}
