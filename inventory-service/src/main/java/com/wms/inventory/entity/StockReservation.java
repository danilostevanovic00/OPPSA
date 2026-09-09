package com.wms.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Records a quantity of an item reserved in a warehouse for a given shipment request.
 */
@Entity
@Table(name = "stock_reservations")
public class StockReservation {

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

    @Column(name = "reserved_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal reservedQuantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }
}
