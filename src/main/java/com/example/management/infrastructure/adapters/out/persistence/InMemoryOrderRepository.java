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
 * Esta es una implementación simple para desarrollo y testing.
 * En producción, debería ser reemplazada por una implementación con persistencia real (JPA, etc.).
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {
    
    private final Map<OrderId, Order> orders = new ConcurrentHashMap<>();
    
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
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        return Optional.ofNullable(orders.get(id));
    }
    
    @Override
    public boolean existsById(OrderId id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        return orders.containsKey(id);
    }
    
    @Override
    public void deleteById(OrderId id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        orders.remove(id);
    }
}
