package com.wms.orders.repository;

import com.wms.orders.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Invoice} entities.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
