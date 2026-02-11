package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void shouldCreatePendingStatus() {
        OrderStatus status = OrderStatus.PENDING;
        
        assertEquals("PENDING", status.name());
    }

    @Test
    void shouldCreateConfirmedStatus() {
        OrderStatus status = OrderStatus.CONFIRMED;
        
        assertEquals("CONFIRMED", status.name());
    }

    @Test
    void shouldCreateShippedStatus() {
        OrderStatus status = OrderStatus.SHIPPED;
        
        assertEquals("SHIPPED", status.name());
    }

    @Test
    void shouldCreateDeliveredStatus() {
        OrderStatus status = OrderStatus.DELIVERED;
        
        assertEquals("DELIVERED", status.name());
    }

    @Test
    void shouldCreateCancelledStatus() {
        OrderStatus status = OrderStatus.CANCELLED;
        
        assertEquals("CANCELLED", status.name());
    }

    @Test
    void shouldAllowTransitionFromPendingToConfirmed() {
        assertTrue(OrderStatus.PENDING.canTransitionTo(OrderStatus.CONFIRMED));
    }

    @Test
    void shouldAllowTransitionFromConfirmedToShipped() {
        assertTrue(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.SHIPPED));
    }

    @Test
    void shouldAllowTransitionFromShippedToDelivered() {
        assertTrue(OrderStatus.SHIPPED.canTransitionTo(OrderStatus.DELIVERED));
    }

    @Test
    void shouldAllowTransitionFromPendingToCancelled() {
        assertTrue(OrderStatus.PENDING.canTransitionTo(OrderStatus.CANCELLED));
    }

    @Test
    void shouldNotAllowTransitionFromDeliveredToShipped() {
        assertFalse(OrderStatus.DELIVERED.canTransitionTo(OrderStatus.SHIPPED));
    }

    @Test
    void shouldNotAllowTransitionFromCancelledToConfirmed() {
        assertFalse(OrderStatus.CANCELLED.canTransitionTo(OrderStatus.CONFIRMED));
    }
}
