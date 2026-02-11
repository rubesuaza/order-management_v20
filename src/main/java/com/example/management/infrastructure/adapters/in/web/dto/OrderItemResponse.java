package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.application.services.dto.OrderItemQueryResult;
import java.math.BigDecimal;

/**
 * DTO para la respuesta de un item de orden.
 */
public record OrderItemResponse(
    String productId,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal total
) {
    public static OrderItemResponse from(OrderItemQueryResult itemResult) {
        return new OrderItemResponse(
            itemResult.productId(),
            itemResult.unitPrice(),
            itemResult.quantity(),
            itemResult.total()
        );
    }
}
