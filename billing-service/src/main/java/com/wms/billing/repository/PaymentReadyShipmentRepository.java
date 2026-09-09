package com.wms.billing.repository;

import com.wms.billing.entity.PaymentReadyShipment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link PaymentReadyShipment} entities.
 */
public interface PaymentReadyShipmentRepository extends JpaRepository<PaymentReadyShipment, String> {
}
