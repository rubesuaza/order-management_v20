package com.example.management.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la actualización del estado de una orden.
 */
public record UpdateOrderStatusRequest(
    @NotBlank(message = "El status es obligatorio")
    String status
) {}
