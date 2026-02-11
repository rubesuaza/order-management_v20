package com.example.management.application.ports.in;

import com.example.management.application.services.dto.OrderQueryResult;

/**
 * Puerto de entrada para el caso de uso de creación de órdenes.
 */
public interface CreateOrderUseCase {
    
    /**
     * Crea una nueva orden.
     * @param command El comando con los datos de la orden a crear
     * @return El resultado de la consulta de la orden creada
     */
    OrderQueryResult createOrder(CreateOrderCommand command);
}
