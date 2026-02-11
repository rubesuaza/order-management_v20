package com.example.management.domain.model;

import java.util.Objects;

/**
 * Value Object que representa el identificador de un producto.
 * Invariante: El valor no puede ser null o vacío.
 */
public class ProductId {
    private final String value;

    public ProductId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
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
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProductId{" +
               "value='" + value + '\'' +
               '}';
    }
}
