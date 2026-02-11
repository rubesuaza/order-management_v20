package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderCommand;
import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusCommand;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderStateException;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.ProductId;
import com.example.management.domain.model.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de órdenes.
 */
@Service
@Transactional
public class OrderService implements CreateOrderUseCase, GetOrderUseCase, UpdateOrderStatusUseCase {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    @Override
    public Order createOrder(CreateOrderCommand command) {
        OrderId orderId = new OrderId(command.orderId());
        
        if (orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException(
                String.format("Ya existe una orden con el id: %s", orderId.getValue())
            );
        }
        
        List<OrderItem> items = command.items().stream()
            .map(itemCommand -> new OrderItem(
                new ProductId(itemCommand.productId()),
                new Money(itemCommand.unitPrice()),
                new Quantity(itemCommand.quantity())
            ))
            .toList();
        
        Order order = new Order(orderId, items);
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(OrderId orderId) {
        return orderRepository.findById(orderId);
    }
    
    @Override
    public Order updateOrderStatus(UpdateOrderStatusCommand command) {
        OrderId orderId = new OrderId(command.orderId());
        OrderStatus newStatus;
        
        try {
            newStatus = OrderStatus.valueOf(command.status().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + command.status());
        }
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la orden con id: " + orderId.getValue()));
        
        try {
            order.changeStatus(newStatus);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(
                String.format("No se puede cambiar el estado de la orden %s a %s", orderId.getValue(), newStatus)
            );
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * Calcula el total de una orden.
     * Este método debe ser invocado por la capa de infraestructura antes de crear el DTO de respuesta.
     * @param order La orden de la cual calcular el total
     * @return El total calculado como Double
     */
    public Double calculateOrderTotal(Order order) {
        return order.calculateTotal().getAmount();
    }
}
