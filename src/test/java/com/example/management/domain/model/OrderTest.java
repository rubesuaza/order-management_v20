package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderWithValidData() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        
        assertEquals(orderId, order.getId());
        assertEquals(1, order.getItems().size());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(null, items);
        });
    }

    @Test
    void shouldThrowExceptionWhenItemsListIsNull() {
        OrderId orderId = new OrderId("ORD-001");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(orderId, null);
        });
    }

    @Test
    void shouldThrowExceptionWhenItemsListIsEmpty() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> emptyItems = new ArrayList<>();
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(orderId, emptyItems);
        });
    }

    @Test
    void shouldCalculateTotalAsSumOfItems() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2)),
            new OrderItem("PROD-002", new Money(5.25), new Quantity(3))
        );
        
        Order order = new Order(orderId, items);
        Money total = order.calculateTotal();
        
        // 10.50 * 2 + 5.25 * 3 = 21.00 + 15.75 = 36.75
        assertEquals(36.75, total.getAmount(), 0.001);
    }

    @Test
    void shouldChangeStatusFromPendingToConfirmed() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        order.changeStatus(OrderStatus.CONFIRMED);
        
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void shouldChangeStatusFromPendingToCancelled() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        order.changeStatus(OrderStatus.CANCELLED);
        
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTransitioningToInvalidStatus() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        // Transiciones válidas hasta DELIVERED
        order.changeStatus(OrderStatus.CONFIRMED);
        order.changeStatus(OrderStatus.SHIPPED);
        order.changeStatus(OrderStatus.DELIVERED);
        
        // Ahora intentar una transición inválida desde DELIVERED
        assertThrows(IllegalStateException.class, () -> {
            order.changeStatus(OrderStatus.SHIPPED);
        });
    }

    @Test
    void shouldNotAllowStatusChangeFromCancelled() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        order.changeStatus(OrderStatus.CANCELLED);
        
        assertThrows(IllegalStateException.class, () -> {
            order.changeStatus(OrderStatus.CONFIRMED);
        });
    }

    @Test
    void shouldNotAllowStatusChangeFromDelivered() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        order.changeStatus(OrderStatus.CONFIRMED);
        order.changeStatus(OrderStatus.SHIPPED);
        order.changeStatus(OrderStatus.DELIVERED);
        
        assertThrows(IllegalStateException.class, () -> {
            order.changeStatus(OrderStatus.SHIPPED);
        });
    }

    @Test
    void shouldAddItemToOrder() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", new Money(10.50), new Quantity(2)));
        
        Order order = new Order(orderId, items);
        OrderItem newItem = new OrderItem("PROD-002", new Money(5.25), new Quantity(1));
        order.addItem(newItem);
        
        assertEquals(2, order.getItems().size());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        
        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(null);
        });
    }

    @Test
    void shouldHaveCreatedAtTimestamp() {
        OrderId orderId = new OrderId("ORD-001");
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", new Money(10.50), new Quantity(2))
        );
        
        Order order = new Order(orderId, items);
        
        assertNotNull(order.getCreatedAt());
        assertTrue(order.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
