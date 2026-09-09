package com.wms.billing.repository;

import com.wms.billing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Payment} entities.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
