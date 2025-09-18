package org.ean.techlogistics.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String code,
        String name,
        String description,
        Integer stock,
        BigDecimal price
) {}
