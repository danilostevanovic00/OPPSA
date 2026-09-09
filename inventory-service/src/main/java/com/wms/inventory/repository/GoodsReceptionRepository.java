package com.wms.inventory.repository;

import com.wms.inventory.entity.GoodsReception;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link GoodsReception} entities.
 */
public interface GoodsReceptionRepository extends JpaRepository<GoodsReception, Long> {
}
