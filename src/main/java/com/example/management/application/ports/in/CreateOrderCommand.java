package com.example.management.application.ports.in;

import java.util.List;

/**
 * DTO de comando para la creación de una orden.
 * Este DTO se usa para desacoplar la capa de infraestructura del dominio.
 */
public record CreateOrderCommand(
    String orderId,
    List<OrderItemCommand> items
) {
    /**
     * DTO de comando para un item de orden.
     */
    public record OrderItemCommand(
        String productId,
        String unitPrice,
        Integer quantity
    ) {}
}
