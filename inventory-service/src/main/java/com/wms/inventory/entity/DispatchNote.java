package com.wms.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record of stock that has physically left a warehouse for a shipment.
 */
@Entity
@Table(name = "dispatch_notes")
public class DispatchNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_request_id", nullable = false, length = 100)
    private String shipmentRequestId;

    @Column(name = "warehouse_code", nullable = false, length = 50)
    private String warehouseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private DispatchNoteStatus status = DispatchNoteStatus.READY_FOR_DISPATCH;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public DispatchNoteStatus getStatus() { return status; }
    public void setStatus(DispatchNoteStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
