package com.wms.inventory.repository;

import com.wms.inventory.entity.Item;
import com.wms.inventory.entity.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link WarehouseStock} entities.
 */
public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {

    List<WarehouseStock> findByItem(Item item);

    Optional<WarehouseStock> findByWarehouseCodeAndItem(String warehouseCode, Item item);
}
