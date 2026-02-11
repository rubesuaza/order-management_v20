package com.example.management.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para la creación de una orden.
 */
public record CreateOrderRequest(
    @NotBlank(message = "El orderId es obligatorio")
    String orderId,
    
    @NotNull(message = "Los items son obligatorios")
    @NotEmpty(message = "La orden debe tener al menos un item")
    List<OrderItemRequest> items
) {}
