package com.example.management.application.ports.out;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de órdenes.
 * Define el contrato que deben cumplir los adaptadores de persistencia.
 */
public interface OrderRepository {
    
    /**
     * Guarda una orden en el repositorio.
     * @param order La orden a guardar
     * @return La orden guardada
     */
    Order save(Order order);
    
    /**
     * Busca una orden por su identificador.
     * @param id El identificador de la orden
     * @return Un Optional con la orden si existe, vacío en caso contrario
     */
    Optional<Order> findById(OrderId id);
    
    /**
     * Verifica si existe una orden con el identificador dado.
     * @param id El identificador de la orden
     * @return true si existe, false en caso contrario
     */
    boolean existsById(OrderId id);
    
    /**
     * Elimina una orden del repositorio.
     * @param id El identificador de la orden a eliminar
     */
    void deleteById(OrderId id);
}
