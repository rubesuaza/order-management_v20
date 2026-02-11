package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderCommand;
import com.example.management.application.ports.in.UpdateOrderStatusCommand;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.dto.OrderQueryResult;
import com.example.management.domain.exception.InvalidOrderStateException;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.ProductId;
import com.example.management.domain.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        CreateOrderCommand command = new CreateOrderCommand(
            "ORD-001",
            List.of(new CreateOrderCommand.OrderItemCommand("PROD-001", BigDecimal.valueOf(10.50), 2))
        );
        
        when(orderRepository.existsById(any(OrderId.class))).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderQueryResult result = orderService.createOrder(command);

        // Assert
        assertNotNull(result);
        assertEquals("ORD-001", result.id());
        assertEquals("PENDING", result.status());
        assertEquals(1, result.items().size());
        verify(orderRepository).existsById(any(OrderId.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderIdAlreadyExists() {
        // Arrange
        CreateOrderCommand command = new CreateOrderCommand(
            "ORD-001",
            List.of(new CreateOrderCommand.OrderItemCommand("PROD-001", BigDecimal.valueOf(10.50), 2))
        );
        
        when(orderRepository.existsById(any(OrderId.class))).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.createOrder(command)
        );

        assertEquals("Ya existe una orden con el id: ORD-001", exception.getMessage());
        verify(orderRepository).existsById(any(OrderId.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldSaveOrderAfterCreation() {
        // Arrange
        CreateOrderCommand command = new CreateOrderCommand(
            "ORD-002",
            List.of(
                new CreateOrderCommand.OrderItemCommand("PROD-001", BigDecimal.valueOf(10.50), 2),
                new CreateOrderCommand.OrderItemCommand("PROD-002", BigDecimal.valueOf(5.25), 3)
            )
        );
        
        when(orderRepository.existsById(any(OrderId.class))).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderQueryResult result = orderService.createOrder(command);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    // ========== Tests para getOrder ==========

    @Test
    void shouldGetOrderWhenExists() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2)))
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act
        Optional<OrderQueryResult> result = orderService.getOrder(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ORD-001", result.get().id());
        assertEquals("PENDING", result.get().status());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        // Arrange
        OrderId orderId = new OrderId("ORD-999");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<OrderQueryResult> result = orderService.getOrder(orderId);

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
            List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2)))
        );
        
        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand("ORD-001", "CONFIRMED");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderQueryResult result = orderService.updateOrderStatus(command);

        // Assert
        assertNotNull(result);
        assertEquals("CONFIRMED", result.status());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFoundForUpdate() {
        // Arrange
        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand("ORD-999", "CONFIRMED");
        
        when(orderRepository.findById(any(OrderId.class))).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.updateOrderStatus(command)
        );

        assertEquals("No se encontró la orden con id: ORD-999", exception.getMessage());
        verify(orderRepository).findById(any(OrderId.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowInvalidOrderStateExceptionWhenTransitionIsInvalid() {
        // Arrange
        OrderId orderId = new OrderId("ORD-001");
        Order existingOrder = new Order(
            orderId,
            List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2)))
        );
        // Cambiar a un estado final que no permite transiciones
        existingOrder.changeStatus(OrderStatus.CANCELLED);
        
        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand("ORD-001", "CONFIRMED");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act & Assert
        InvalidOrderStateException exception = assertThrows(
            InvalidOrderStateException.class,
            () -> orderService.updateOrderStatus(command)
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
            List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2)))
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            return saved;
        });

        // Act - Transición válida: PENDING -> CONFIRMED
        OrderQueryResult result1 = orderService.updateOrderStatus(new UpdateOrderStatusCommand("ORD-001", "CONFIRMED"));
        assertEquals("CONFIRMED", result1.status());

        // Simular que la orden ya está confirmada
        Order confirmedOrder = new Order(orderId, List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2))));
        confirmedOrder.changeStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(confirmedOrder));
        
        // Act - Transición válida: CONFIRMED -> SHIPPED
        OrderQueryResult result2 = orderService.updateOrderStatus(new UpdateOrderStatusCommand("ORD-001", "SHIPPED"));
        assertEquals("SHIPPED", result2.status());

        // Simular que la orden ya está enviada
        Order shippedOrder = new Order(orderId, List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2))));
        shippedOrder.changeStatus(OrderStatus.CONFIRMED);
        shippedOrder.changeStatus(OrderStatus.SHIPPED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(shippedOrder));
        
        // Act - Transición válida: SHIPPED -> DELIVERED
        OrderQueryResult result3 = orderService.updateOrderStatus(new UpdateOrderStatusCommand("ORD-001", "DELIVERED"));
        assertEquals("DELIVERED", result3.status());

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
            List.of(new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2)))
        );
        // Cambiar el estado paso a paso hasta DELIVERED (estado final)
        existingOrder.changeStatus(OrderStatus.CONFIRMED);
        existingOrder.changeStatus(OrderStatus.SHIPPED);
        existingOrder.changeStatus(OrderStatus.DELIVERED);
        
        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand("ORD-001", "SHIPPED");
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act & Assert
        assertThrows(
            InvalidOrderStateException.class,
            () -> orderService.updateOrderStatus(command)
        );

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }
}
