package com.wms.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Single line item of a {@link GoodsReception}.
 */
@Entity
@Table(name = "goods_reception_items")
public class GoodsReceptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_id", nullable = false)
    private GoodsReception reception;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure;

    @Column(name = "purchase_price", nullable = false, precision = 15, scale = 4)
    private BigDecimal purchasePrice;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GoodsReception getReception() { return reception; }
    public void setReception(GoodsReception reception) { this.reception = reception; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
}
