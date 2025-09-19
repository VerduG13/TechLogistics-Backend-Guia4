package org.ean.techlogistics.repository;

import org.ean.techlogistics.entity.Order;
import org.ean.techlogistics.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByClientId(Long clientId);

    @Query("select o from Order o where o.status = :status and o.delivery is null")
    Page<Order> findUnassignedByStatus(OrderStatus status, Pageable pageable);
}
