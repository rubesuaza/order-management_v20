package com.example.management.domain.model;

import java.util.Objects;

/**
 * Value Object que representa el identificador único de una orden.
 * Invariante: El valor no puede ser null, vacío o solo espacios en blanco.
 */
public class OrderId {
    private final String value;

    public OrderId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("OrderId no puede ser null o vacío");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return Objects.equals(value, orderId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
