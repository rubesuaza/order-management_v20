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
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
            .map(OrderItemResponse::from)
            .toList();
        
        return new OrderResponse(
            order.getId().getValue(),
            order.getStatus().name(),
            order.calculateTotal().getAmount(),
            order.getCreatedAt(),
            items
        );
    }
}
