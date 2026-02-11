package com.example.management.application.services.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resultado de consulta de orden en la capa de aplicación.
 * Este DTO se usa para transferir datos de orden desde la capa de aplicación
 * a la capa de infraestructura sin exponer directamente los modelos de dominio.
 */
public record OrderQueryResult(
    String id,
    String status,
    BigDecimal total,
    LocalDateTime createdAt,
    List<OrderItemQueryResult> items
) {}
