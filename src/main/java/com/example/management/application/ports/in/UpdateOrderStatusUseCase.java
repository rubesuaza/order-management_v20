package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderStatus;

/**
 * Puerto de entrada para el caso de uso de actualización del estado de una orden.
 */
public interface UpdateOrderStatusUseCase {
    
    /**
     * Actualiza el estado de una orden.
     * @param orderId El identificador de la orden
     * @param newStatus El nuevo estado
     * @return La orden actualizada
     * @throws com.example.management.domain.exception.InvalidOrderStateException si la transición no es válida
     */
    Order updateOrderStatus(OrderId orderId, OrderStatus newStatus);
}
