package com.example.management.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad raíz del agregado Order.
 * Invariantes:
 * - El id no puede ser null
 * - La lista de items no puede ser null o vacía
 * - El total de la orden debe ser igual a la suma de los totales de los items
 * - Las transiciones de estado deben seguir las reglas de negocio definidas
 */
public class Order {
    private final OrderId id;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;

    public Order(OrderId id, List<OrderItem> items) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la orden no puede ser null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La orden debe tener al menos un item");
        }
        
        this.id = id;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public OrderId getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Calcula el total de la orden sumando el total de todos los items.
     * Invariante: El total debe ser igual a la suma de los items.
     */
    public Money calculateTotal() {
        return items.stream()
                .map(OrderItem::calculateTotal)
                .reduce(new Money(0L), Money::add);
    }

    /**
     * Cambia el estado de la orden validando las transiciones permitidas.
     * Invariante: Solo se permiten transiciones válidas según las reglas de negocio.
     */
    public void changeStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser null");
        }
        
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                String.format("No se puede cambiar el estado de %s a %s", this.status, newStatus)
            );
        }
        
        this.status = newStatus;
    }

    /**
     * Agrega un item a la orden.
     */
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser null");
        }
        items.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", itemsCount=" + items.size() +
                ", createdAt=" + createdAt +
                '}';
    }
}
