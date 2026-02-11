package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void shouldCreateProductIdWithValidValue() {
        ProductId productId = new ProductId("PROD-001");
        
        assertEquals("PROD-001", productId.getValue());
    }

    @Test
    void shouldTrimWhitespaceFromValue() {
        ProductId productId = new ProductId("  PROD-001  ");
        
        assertEquals("PROD-001", productId.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductId(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductId("");
        });
    }

    @Test
    void shouldThrowExceptionWhenValueIsOnlyWhitespace() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductId("   ");
        });
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        ProductId productId1 = new ProductId("PROD-001");
        ProductId productId2 = new ProductId("PROD-001");
        
        assertEquals(productId1, productId2);
        assertEquals(productId1.hashCode(), productId2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        ProductId productId1 = new ProductId("PROD-001");
        ProductId productId2 = new ProductId("PROD-002");
        
        assertNotEquals(productId1, productId2);
    }

    @Test
    void shouldHaveCorrectToString() {
        ProductId productId = new ProductId("PROD-001");
        String toString = productId.toString();
        
        assertTrue(toString.contains("ProductId"));
        assertTrue(toString.contains("PROD-001"));
    }
}
