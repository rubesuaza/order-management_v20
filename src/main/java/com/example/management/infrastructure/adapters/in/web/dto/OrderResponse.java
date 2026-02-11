package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.application.services.dto.OrderItemQueryResult;
import com.example.management.application.services.dto.OrderQueryResult;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden.
 */
public record OrderResponse(
    String id,
    String status,
    java.math.BigDecimal total,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {
    /**
     * Crea un OrderResponse desde un OrderQueryResult de la capa de aplicación.
     * @param orderResult El resultado de consulta de orden de la capa de aplicación
     * @return OrderResponse con los datos de la orden
     */
    public static OrderResponse from(OrderQueryResult orderResult) {
        List<OrderItemResponse> items = orderResult.items().stream()
            .map(OrderItemResponse::from)
            .toList();
        
        return new OrderResponse(
            orderResult.id(),
            orderResult.status(),
            orderResult.total(),
            orderResult.createdAt(),
            items
        );
    }
}
