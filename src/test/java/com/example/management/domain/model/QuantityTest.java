package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void shouldCreateQuantityWithValidValue() {
        Quantity quantity = new Quantity(5);
        
        assertEquals(5, quantity.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quantity(0);
        });
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quantity(-1);
        });
    }

    @Test
    void shouldAddTwoQuantities() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(3);
        
        Quantity result = q1.add(q2);
        
        assertEquals(8, result.getValue());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(5);
        
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(3);
        
        assertNotEquals(q1, q2);
    }
}
