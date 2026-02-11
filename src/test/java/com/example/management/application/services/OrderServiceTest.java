package com.example.management.application.services;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderStateException;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para OrderService.
 * Prueba los casos de uso de creación, obtención y actualización de estado de órdenes.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository);
    }

    // ========== Tests para createOrder ==========

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        when(orderRepository.existsById(orderId)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.createOrder(orderId, items);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(1, result.getItems().size());
        verify(orderRepository).existsById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderIdAlreadyExists() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        when(orderRepository.existsById(orderId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.createOrder(orderId, items)
        );

        assertEquals("Ya existe una orden con el id: ORD-001", exception.getMessage());
        verify(orderRepository).existsById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldSaveOrderAfterCreation() {
        // Arrange
        OrderId orderId = new OrderId("ORD-002");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2)),
            new OrderItem("PROD-002", new Money(5.25), new Quantity(3))
        );
        
        when(orderRepository.existsById(orderId)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.createOrder(orderId, items);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(result);
    }

    // ========== Tests para getOrder ==========

    @Test
    void shouldGetOrderWhenExists() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)))
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act
        Optional<Order> result = orderService.getOrder(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingOrder, result.get());
        assertEquals(orderId, result.get().getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        // Arrange
        OrderId orderId = new OrderId("ORD-999");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = orderService.getOrder(orderId);

        // Assert
        assertTrue(result.isEmpty());
        verify(orderRepository).findById(orderId);
    }

    // ========== Tests para updateOrderStatus ==========

    @Test
    void shouldUpdateOrderStatusSuccessfully() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)))
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFoundForUpdate() {
        // Arrange
        OrderId orderId = new OrderId("ORD-999");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED)
        );

        assertEquals("No se encontró la orden con id: ORD-999", exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowInvalidOrderStateExceptionWhenTransitionIsInvalid() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)))
        );
        // Cambiar a un estado final que no permite transiciones
        existingOrder.changeStatus(OrderStatus.CANCELLED);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act & Assert
        InvalidOrderStateException exception = assertThrows(
            InvalidOrderStateException.class,
            () -> orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED)
        );

        assertTrue(exception.getMessage().contains("No se puede cambiar el estado de la orden ORD-001 a CONFIRMED"));
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldUpdateStatusThroughValidTransitions() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)))
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act - Transición válida: PENDING -> CONFIRMED
        Order result1 = orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, result1.getStatus());

        // Simular que la orden ya está confirmada
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(result1));
        
        // Act - Transición válida: CONFIRMED -> SHIPPED
        Order result2 = orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);
        assertEquals(OrderStatus.SHIPPED, result2.getStatus());

        // Simular que la orden ya está enviada
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(result2));
        
        // Act - Transición válida: SHIPPED -> DELIVERED
        Order result3 = orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED, result3.getStatus());

        // Assert
        verify(orderRepository, times(3)).findById(orderId);
        verify(orderRepository, times(3)).save(any(Order.class));
    }

    @Test
    void shouldNotSaveOrderWhenStatusTransitionFails() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)))
        );
        // Cambiar el estado paso a paso hasta DELIVERED (estado final)
        existingOrder.changeStatus(OrderStatus.CONFIRMED);
        existingOrder.changeStatus(OrderStatus.SHIPPED);
        existingOrder.changeStatus(OrderStatus.DELIVERED);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act & Assert
        assertThrows(
            InvalidOrderStateException.class,
            () -> orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED)
        );

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }
}
