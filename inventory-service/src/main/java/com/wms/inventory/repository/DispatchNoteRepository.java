package com.wms.inventory.repository;

import com.wms.inventory.entity.DispatchNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link DispatchNote} entities.
 */
public interface DispatchNoteRepository extends JpaRepository<DispatchNote, Long> {
    List<DispatchNote> findByShipmentRequestId(String shipmentRequestId);
}
