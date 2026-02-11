package com.example.management.application.services.dto;

import java.math.BigDecimal;

/**
 * DTO de resultado de consulta de item de orden en la capa de aplicación.
 * Este DTO se usa para transferir datos de item de orden desde la capa de aplicación
 * a la capa de infraestructura sin exponer directamente los modelos de dominio.
 */
public record OrderItemQueryResult(
    String productId,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal total
) {}
