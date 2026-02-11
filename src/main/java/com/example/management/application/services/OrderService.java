package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderStateException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
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
    public Order createOrder(OrderId orderId, List<OrderItem> items) {
        if (orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Ya existe una orden con el id: " + orderId.getValue());
        }
        
        Order order = new Order(orderId, items);
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(OrderId orderId) {
        return orderRepository.findById(orderId);
    }
    
    @Override
    public Order updateOrderStatus(OrderId orderId, OrderStatus newStatus) {
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
}
