package com.example.management.domain.model;

import java.util.Objects;

/**
 * Entidad que representa un item dentro de una orden.
 * Invariantes:
 * - El productId no puede ser null o vacío
 * - El unitPrice no puede ser null
 * - La quantity no puede ser null
 */
public class OrderItem {
    private final String productId;
    private final Money unitPrice;
    private final Quantity quantity;

    public OrderItem(String productId, Money unitPrice, Quantity quantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("El productId no puede ser null o vacío");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("El unitPrice no puede ser null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("La quantity no puede ser null");
        }
        
        this.productId = productId.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Calcula el total de este item (precio unitario * cantidad).
     */
    public Money calculateTotal() {
        return unitPrice.multiply(quantity.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId) &&
               Objects.equals(unitPrice, orderItem.unitPrice) &&
               Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, unitPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + productId + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
