package com.wms.inventory.repository;

import com.wms.inventory.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Item} entities.
 */
public interface ItemRepository extends JpaRepository<Item, String> {
}
