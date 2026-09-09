package com.wms.billing.controller;

import com.wms.billing.dto.PayShipmentRequest;
import com.wms.billing.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing billing/payment endpoints.
 */
@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private static final Logger log = LoggerFactory.getLogger(BillingController.class);

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> payShipment(@RequestBody PayShipmentRequest request) {
        log.info("[REST] POST /api/billing/pay - shipmentRequestId={}, paymentDate={}",
                request.getShipmentRequestId(), request.getPaymentDate());
        billingService.payShipment(request);
        return ResponseEntity.ok().build();
    }
}
