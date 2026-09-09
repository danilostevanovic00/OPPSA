package com.wms.orders.repository;

import com.wms.orders.entity.ShipmentRequest;
import com.wms.orders.entity.ShipmentRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link ShipmentRequest} entities.
 */
public interface ShipmentRequestRepository extends JpaRepository<ShipmentRequest, String> {

    List<ShipmentRequest> findByStatusAndPaymentDeadlineBefore(
            ShipmentRequestStatus status, LocalDate date);
}
