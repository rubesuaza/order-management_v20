package com.example.management.domain.model;

/**
 * Value Object que representa el estado de una orden.
 * Define las transiciones válidas entre estados.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    /**
     * Verifica si es posible realizar una transición desde este estado al estado destino.
     */
    public boolean canTransitionTo(OrderStatus target) {
        if (this == target) {
            return true; // Permite mantener el mismo estado
        }

        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == DELIVERED;
            case DELIVERED, CANCELLED -> false; // Estados finales, no se puede cambiar
        };
    }
}
