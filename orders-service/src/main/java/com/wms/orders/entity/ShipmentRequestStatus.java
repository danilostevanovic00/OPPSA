package com.wms.orders.entity;

/**
 * Lifecycle status of a {@link ShipmentRequest}.
 */
public enum ShipmentRequestStatus {
    PENDING,
    PAYMENT_READY,
    COMPLETED,
    CANCELLED,
    EXPIRED
}
