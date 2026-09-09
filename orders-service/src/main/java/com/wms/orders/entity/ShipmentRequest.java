package com.wms.orders.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the fulfillment and payment saga for a confirmed order.
 */
@Entity
@Table(name = "shipment_requests")
public class ShipmentRequest {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_deadline", nullable = false)
    private LocalDate paymentDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ShipmentRequestStatus status = ShipmentRequestStatus.PENDING;

    @OneToMany(mappedBy = "shipmentRequest", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShipmentRequestItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "shipmentRequest", cascade = CascadeType.ALL)
    private Invoice invoice;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDate getPaymentDeadline() { return paymentDeadline; }
    public void setPaymentDeadline(LocalDate paymentDeadline) { this.paymentDeadline = paymentDeadline; }

    public ShipmentRequestStatus getStatus() { return status; }
    public void setStatus(ShipmentRequestStatus status) { this.status = status; }

    public List<ShipmentRequestItem> getItems() { return items; }
    public void setItems(List<ShipmentRequestItem> items) { this.items = items; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}
