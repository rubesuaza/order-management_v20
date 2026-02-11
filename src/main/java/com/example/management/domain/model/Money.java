package com.example.management.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object que representa una cantidad monetaria.
 * Invariante: El monto no puede ser negativo.
 */
public class Money {
    private final BigDecimal amount;

    /**
     * Constructor que acepta un String para garantizar precisión exacta.
     * @param amount El monto como String (ej: "100.50")
     */
    public Money(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("El monto no puede ser null o vacío");
        }
        BigDecimal parsedAmount = new BigDecimal(amount.trim());
        if (parsedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.amount = parsedAmount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Constructor que acepta centavos como long para garantizar precisión exacta.
     * @param cents El monto en centavos (ej: 10050 para representar 100.50)
     */
    public Money(long cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.amount = BigDecimal.valueOf(cents, 2).setScale(2, RoundingMode.HALF_UP);
    }

    private Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (quantity == 0) {
            return new Money(0L);
        }
        if (quantity == 1) {
            return this;
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
