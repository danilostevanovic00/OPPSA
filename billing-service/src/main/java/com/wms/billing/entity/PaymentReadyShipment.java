package com.wms.billing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Local cache of shipment requests that are currently ready for payment, synced via RabbitMQ events from orders-service.
 */
@Entity
@Table(name = "payment_ready_shipments")
public class PaymentReadyShipment {

    @Id
    @Column(name = "shipment_request_id", length = 100)
    private String shipmentRequestId;

    @Column(name = "ready_at", nullable = false)
    private LocalDateTime readyAt = LocalDateTime.now();

    public String getShipmentRequestId() { return shipmentRequestId; }
    public void setShipmentRequestId(String shipmentRequestId) { this.shipmentRequestId = shipmentRequestId; }

    public LocalDateTime getReadyAt() { return readyAt; }
    public void setReadyAt(LocalDateTime readyAt) { this.readyAt = readyAt; }
}
