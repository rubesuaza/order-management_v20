package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden.
 */
public record OrderResponse(
    String id,
    String status,
    Double total,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {
    /**
     * Crea un OrderResponse desde un Order y su total calculado.
     * El total debe ser calculado previamente en la capa de aplicación.
     * @param order La orden del dominio
     * @param total El total calculado de la orden
     * @return OrderResponse con los datos de la orden
     */
    public static OrderResponse from(Order order, Double total) {
        List<OrderItemResponse> items = order.getItems().stream()
            .map(OrderItemResponse::from)
            .toList();
        
        return new OrderResponse(
            order.getId().getValue(),
            order.getStatus().name(),
            total,
            order.getCreatedAt(),
            items
        );
    }
}
