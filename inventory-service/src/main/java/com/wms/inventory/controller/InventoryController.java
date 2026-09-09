package com.wms.inventory.controller;

import com.wms.inventory.dto.*;
import com.wms.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing inventory item and stock endpoints.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        log.info("[REST] GET /api/inventory/items");
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/items/{itemCode}/state")
    public ResponseEntity<List<WarehouseStockResponse>> getItemState(@PathVariable String itemCode) {
        log.info("[REST] GET /api/inventory/items/{}/state", itemCode);
        return ResponseEntity.ok(inventoryService.getItemState(itemCode));
    }

    @PostMapping("/items")
    public ResponseEntity<Void> newItem(@RequestBody NewItemRequest request) {
        log.info("[REST] POST /api/inventory/items - itemCode={}, name={}",
                request.getItemCode(), request.getName());
        inventoryService.newItem(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reception")
    public ResponseEntity<Void> receptionOfGoods(@RequestBody ReceptionRequest request) {
        log.info("[REST] POST /api/inventory/reception - warehouse={}, supplier={}, items={}",
                request.getWarehouseCode(), request.getSupplierCode(), request.getItems().size());
        inventoryService.receptionOfGoods(request);
        return ResponseEntity.ok().build();
    }
}
