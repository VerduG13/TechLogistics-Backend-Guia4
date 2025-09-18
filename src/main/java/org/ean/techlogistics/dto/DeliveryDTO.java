package org.ean.techlogistics.dto;

import java.time.LocalDateTime;

public record DeliveryDTO(
        Long id,
        LocalDateTime pickedUpDate,
        LocalDateTime deliveryDate,
        Long courierId,
        String courierEmail
) {}
