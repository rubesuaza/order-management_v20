package com.example.management.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para la creación de un item de orden.
 */
public record OrderItemRequest(
    @NotBlank(message = "El productId es obligatorio")
    String productId,
    
    @NotNull(message = "El unitPrice es obligatorio")
    @Positive(message = "El unitPrice debe ser positivo")
    Double unitPrice,
    
    @NotNull(message = "La quantity es obligatoria")
    @Positive(message = "La quantity debe ser positiva")
    Integer quantity
) {}
