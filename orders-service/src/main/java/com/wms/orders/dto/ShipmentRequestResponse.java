package com.wms.orders.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response payload representing a shipment request.
 */
public class ShipmentRequestResponse {
    private String id;
    private String orderId;
    private BigDecimal totalAmount;
    private LocalDate paymentDeadline;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDate getPaymentDeadline() { return paymentDeadline; }
    public void setPaymentDeadline(LocalDate paymentDeadline) { this.paymentDeadline = paymentDeadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
