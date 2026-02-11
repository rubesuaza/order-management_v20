package com.example.management.application.ports.in;

/**
 * DTO de comando para la actualización del estado de una orden.
 * Este DTO se usa para desacoplar la capa de infraestructura del dominio.
 */
public record UpdateOrderStatusCommand(
    String orderId,
    String status
) {}
