package org.ean.techlogistics.dto;

public record UserDTO(
        Long id,
        String email,
        String role,
        String phoneNumber,
        String name
) {}
