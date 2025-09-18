package org.ean.techlogistics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        String code,
        String address,
        String status,
        LocalDateTime confirmationDate,
        UserDTO client,
        List<OrderItemDTO> items,
        DeliveryDTO delivery,
        BigDecimal total
) {}
