package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de contrato para el adaptador de repositorio de órdenes.
 * Verifica que cualquier implementación del OrderRepository cumpla con el contrato esperado.
 */
class InMemoryOrderRepositoryTest {
    
    private InMemoryOrderRepository repository;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }
    
    @Test
    void shouldSaveOrder() {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderItem item = new OrderItem("PROD-001", new Money(10.0), new Quantity(2));
        Order order = new Order(orderId, List.of(item));
        
        // When
        Order savedOrder = repository.save(order);
        
        // Then
        assertNotNull(savedOrder);
        assertEquals(orderId, savedOrder.getId());
        assertTrue(repository.existsById(orderId));
    }
    
    @Test
    void shouldFindOrderById() {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderItem item = new OrderItem("PROD-001", new Money(10.0), new Quantity(2));
        Order order = new Order(orderId, List.of(item));
        repository.save(order);
        
        // When
        Optional<Order> foundOrder = repository.findById(orderId);
        
        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(orderId, foundOrder.get().getId());
    }
    
    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        // Given
        OrderId orderId = new OrderId("ORDER-NOT-FOUND");
        
        // When
        Optional<Order> foundOrder = repository.findById(orderId);
        
        // Then
        assertFalse(foundOrder.isPresent());
    }
    
    @Test
    void shouldReturnTrueWhenOrderExists() {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderItem item = new OrderItem("PROD-001", new Money(10.0), new Quantity(2));
        Order order = new Order(orderId, List.of(item));
        repository.save(order);
        
        // When
        boolean exists = repository.existsById(orderId);
        
        // Then
        assertTrue(exists);
    }
    
    @Test
    void shouldReturnFalseWhenOrderDoesNotExist() {
        // Given
        OrderId orderId = new OrderId("ORDER-NOT-FOUND");
        
        // When
        boolean exists = repository.existsById(orderId);
        
        // Then
        assertFalse(exists);
    }
    
    @Test
    void shouldDeleteOrderById() {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderItem item = new OrderItem("PROD-001", new Money(10.0), new Quantity(2));
        Order order = new Order(orderId, List.of(item));
        repository.save(order);
        assertTrue(repository.existsById(orderId));
        
        // When
        repository.deleteById(orderId);
        
        // Then
        assertFalse(repository.existsById(orderId));
        assertFalse(repository.findById(orderId).isPresent());
    }
    
    @Test
    void shouldUpdateOrderWhenSavingExistingOrder() {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderItem item1 = new OrderItem("PROD-001", new Money(10.0), new Quantity(2));
        Order order1 = new Order(orderId, List.of(item1));
        repository.save(order1);
        
        // When - Actualizar la orden con un nuevo item
        OrderItem item2 = new OrderItem("PROD-002", new Money(20.0), new Quantity(1));
        Order order2 = repository.findById(orderId).orElseThrow();
        order2.addItem(item2);
        Order updatedOrder = repository.save(order2);
        
        // Then
        assertEquals(2, updatedOrder.getItems().size());
        assertTrue(repository.existsById(orderId));
    }
}
