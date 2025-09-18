package org.ean.techlogistics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


public enum UserRole {
    ADMIN,
    CLIENT,
    COURIER
}
