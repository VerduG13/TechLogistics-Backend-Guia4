package org.ean.techlogistics.repository;

import org.ean.techlogistics.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findByCourierId(Long courierId, Pageable pageable);

    Optional<Delivery> findByOrderId(Long orderId);
}