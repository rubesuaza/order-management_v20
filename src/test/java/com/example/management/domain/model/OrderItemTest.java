package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithValidData() {
        ProductId productId = new ProductId("PROD-001");
        Money unitPrice = new Money("10.50");
        Quantity quantity = new Quantity(2);
        
        OrderItem item = new OrderItem(productId, unitPrice, quantity);
        
        assertEquals(productId, item.getProductId());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        Money unitPrice = new Money("10.50");
        Quantity quantity = new Quantity(3);
        OrderItem item = new OrderItem(new ProductId("PROD-001"), unitPrice, quantity);
        
        Money total = item.calculateTotal();
        
        assertEquals(31.50, total.getAmount(), 0.001);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        Money unitPrice = new Money("10.50");
        Quantity quantity = new Quantity(2);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(null, unitPrice, quantity);
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNull() {
        Quantity quantity = new Quantity(2);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(new ProductId("PROD-001"), null, quantity);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        Money unitPrice = new Money("10.50");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(new ProductId("PROD-001"), unitPrice, null);
        });
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreSame() {
        OrderItem item1 = new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2));
        OrderItem item2 = new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2));
        
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenProductIdIsDifferent() {
        OrderItem item1 = new OrderItem(new ProductId("PROD-001"), new Money("10.50"), new Quantity(2));
        OrderItem item2 = new OrderItem(new ProductId("PROD-002"), new Money("10.50"), new Quantity(2));
        
        assertNotEquals(item1, item2);
    }
}
