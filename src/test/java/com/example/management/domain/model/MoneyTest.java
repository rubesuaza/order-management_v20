package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidAmountFromString() {
        Money money = new Money("100.50");
        
        assertEquals(100.50, money.getAmount(), 0.001);
    }

    @Test
    void shouldCreateMoneyWithValidAmountFromCents() {
        Money money = new Money(10050L); // 100.50 en centavos
        
        assertEquals(100.50, money.getAmount(), 0.001);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegativeFromString() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money("-10.0");
        });
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegativeFromCents() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(-1000L);
        });
    }

    @Test
    void shouldAllowZeroAmount() {
        Money money = new Money(0L);
        
        assertEquals(0.0, money.getAmount(), 0.001);
    }

    @Test
    void shouldAddTwoMoneyAmounts() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        
        Money result = money1.add(money2);
        
        assertEquals(150.75, result.getAmount(), 0.001);
    }

    @Test
    void shouldMultiplyMoneyByQuantity() {
        Money money = new Money("10.50");
        int quantity = 3;
        
        Money result = money.multiply(quantity);
        
        assertEquals(31.50, result.getAmount(), 0.001);
    }

    @Test
    void shouldThrowExceptionWhenMultiplyingByNegativeQuantity() {
        Money money = new Money("10.50");
        
        assertThrows(IllegalArgumentException.class, () -> {
            money.multiply(-1);
        });
    }

    @Test
    void shouldBeEqualWhenAmountsAreSame() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("100.50");
        
        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenAmountsAreDifferent() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("100.51");
        
        assertNotEquals(money1, money2);
    }
}
