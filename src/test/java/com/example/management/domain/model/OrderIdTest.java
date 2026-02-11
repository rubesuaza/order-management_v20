package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderIdTest {

    @Test
    void shouldCreateOrderIdWithValidValue() {
        String value = "ORD-12345";
        OrderId orderId = new OrderId(value);
        
        assertEquals(value, orderId.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderId(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderId("");
        });
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderId("   ");
        });
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        OrderId id1 = new OrderId("ORD-12345");
        OrderId id2 = new OrderId("ORD-12345");
        
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        OrderId id1 = new OrderId("ORD-12345");
        OrderId id2 = new OrderId("ORD-67890");
        
        assertNotEquals(id1, id2);
    }
}
