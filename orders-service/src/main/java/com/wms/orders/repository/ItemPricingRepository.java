package com.wms.orders.repository;

import com.wms.orders.entity.ItemPricing;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link ItemPricing} entities.
 */
public interface ItemPricingRepository extends JpaRepository<ItemPricing, String> {
}
