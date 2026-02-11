package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import java.util.List;

/**
 * Puerto de entrada para el caso de uso de creación de órdenes.
 */
public interface CreateOrderUseCase {
    
    /**
     * Crea una nueva orden.
     * @param orderId El identificador de la orden
     * @param items La lista de items de la orden
     * @return La orden creada
     */
    Order createOrder(OrderId orderId, List<OrderItem> items);
}
