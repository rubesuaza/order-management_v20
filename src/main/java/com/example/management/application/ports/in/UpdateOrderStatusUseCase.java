package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

/**
 * Puerto de entrada para el caso de uso de actualización del estado de una orden.
 */
public interface UpdateOrderStatusUseCase {
    
    /**
     * Actualiza el estado de una orden.
     * @param command El comando con el id de la orden y el nuevo estado
     * @return La orden actualizada
     * @throws com.example.management.domain.exception.InvalidOrderStateException si la transición no es válida
     */
    Order updateOrderStatus(UpdateOrderStatusCommand command);
}
