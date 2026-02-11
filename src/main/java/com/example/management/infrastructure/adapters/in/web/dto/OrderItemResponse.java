package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.domain.model.OrderItem;

/**
 * DTO para la respuesta de un item de orden.
 */
public record OrderItemResponse(
    String productId,
    Double unitPrice,
    Integer quantity,
    Double total
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
            item.getProductId().getValue(),
            item.getUnitPrice().getAmount(),
            item.getQuantity().getValue(),
            item.calculateTotal().getAmount()
        );
    }
}
