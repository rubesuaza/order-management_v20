package com.example.management.application.ports.in;

import com.example.management.application.services.dto.OrderQueryResult;
import com.example.management.domain.model.OrderId;
import java.util.Optional;

/**
 * Puerto de entrada para el caso de uso de obtención de órdenes.
 */
public interface GetOrderUseCase {
    
    /**
     * Obtiene una orden por su identificador.
     * @param orderId El identificador de la orden
     * @return Un Optional con el resultado de la consulta de orden si existe, vacío en caso contrario
     */
    Optional<OrderQueryResult> getOrder(OrderId orderId);
}
