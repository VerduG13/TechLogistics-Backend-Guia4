package org.ean.techlogistics.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Integer qty,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}