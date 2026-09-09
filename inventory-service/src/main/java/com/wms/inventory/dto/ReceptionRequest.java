package com.wms.inventory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Request payload for recording a goods reception.
 */
public class ReceptionRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String supplierCode;
    private String warehouseCode;
    private List<ReceptionItemRequest> items;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public List<ReceptionItemRequest> getItems() { return items; }
    public void setItems(List<ReceptionItemRequest> items) { this.items = items; }
}
