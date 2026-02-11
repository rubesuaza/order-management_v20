package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

/**
 * Puerto de entrada para el caso de uso de creación de órdenes.
 */
public interface CreateOrderUseCase {
    
    /**
     * Crea una nueva orden.
     * @param command El comando con los datos de la orden a crear
     * @return La orden creada
     */
    Order createOrder(CreateOrderCommand command);
}
