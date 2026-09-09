package com.wms.inventory.repository;

import com.wms.inventory.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link StockReservation} entities.
 */
public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    List<StockReservation> findByShipmentRequestId(String shipmentRequestId);
}
