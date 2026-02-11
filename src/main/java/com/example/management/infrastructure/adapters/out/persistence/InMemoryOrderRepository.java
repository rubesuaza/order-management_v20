package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria del repositorio de órdenes.
 * 
 * IMPORTANTE: Esta implementación es SOLO para desarrollo, testing y demostración.
 * NO debe utilizarse en entornos de producción ya que:
 * - Los datos se pierden al reiniciar la aplicación
 * - No proporciona persistencia real de datos
 * - No garantiza integridad de datos en caso de fallos
 * - No escala para múltiples instancias de la aplicación
 * 
 * Para producción, debe implementarse una versión que utilice un mecanismo de
 * persistencia real (JPA con base de datos relacional, NoSQL, etc.).
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {
    
    private final Map<OrderId, Order> orders = new ConcurrentHashMap<>();
    
    private void validateOrderId(OrderId id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
    }
    
    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("La orden no puede ser null");
        }
        orders.put(order.getId(), order);
        return order;
    }
    
    @Override
    public Optional<Order> findById(OrderId id) {
        validateOrderId(id);
        return Optional.ofNullable(orders.get(id));
    }
    
    @Override
    public boolean existsById(OrderId id) {
        validateOrderId(id);
        return orders.containsKey(id);
    }
    
    @Override
    public void deleteById(OrderId id) {
        validateOrderId(id);
        orders.remove(id);
    }
}
