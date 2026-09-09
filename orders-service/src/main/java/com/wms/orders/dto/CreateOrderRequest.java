package com.wms.orders.dto;

import java.util.List;

/**
 * Request payload for creating a new order.
 */
public class CreateOrderRequest {
    private String customerCode;
    private String customerName;
    private String deliveryAddress;
    private List<CreateOrderItemRequest> items;

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public List<CreateOrderItemRequest> getItems() { return items; }
    public void setItems(List<CreateOrderItemRequest> items) { this.items = items; }
}
