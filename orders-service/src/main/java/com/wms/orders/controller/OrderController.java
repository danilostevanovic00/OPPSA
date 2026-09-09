package com.wms.orders.controller;

import com.wms.orders.dto.*;
import com.wms.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST controller exposing order and shipment request endpoints.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("[REST] POST /api/orders - customer={}, items={}",
                request.getCustomerCode(), request.getItems().size());
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ShipmentRequestResponse> confirmOrder(@PathVariable String orderId) {
        log.info("[REST] POST /api/orders/{}/confirm", orderId);
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PutMapping("/pricing/{itemCode}/margin")
    public ResponseEntity<Void> setCustomMargin(
            @PathVariable String itemCode,
            @RequestBody Map<String, BigDecimal> body) {
        log.info("[REST] PUT /api/orders/pricing/{}/margin - margin={}", itemCode, body.get("marginPercent"));
        orderService.setCustomMargin(itemCode, body.get("marginPercent"));
        return ResponseEntity.ok().build();
    }
}
