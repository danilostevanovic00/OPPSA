package com.wms.orders.repository;

import com.wms.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Order} entities.
 */
public interface OrderRepository extends JpaRepository<Order, String> {
}
