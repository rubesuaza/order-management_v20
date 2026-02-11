package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderCommand;
import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusCommand;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.dto.OrderItemQueryResult;
import com.example.management.application.services.dto.OrderQueryResult;
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
    public OrderQueryResult createOrder(CreateOrderCommand command) {
        OrderId orderId = new OrderId(command.orderId());
        
        if (orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException(
                String.format("Ya existe una orden con el id: %s", orderId.getValue())
            );
        }
        
        List<OrderItem> items = command.items().stream()
            .map(itemCommand -> new OrderItem(
                new ProductId(itemCommand.productId()),
                new Money(itemCommand.unitPrice().toString()),
                new Quantity(itemCommand.quantity())
            ))
            .toList();
        
        Order order = new Order(orderId, items);
        Order savedOrder = orderRepository.save(order);
        return toOrderQueryResult(savedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderQueryResult> getOrder(OrderId orderId) {
        return orderRepository.findById(orderId)
            .map(this::toOrderQueryResult);
    }
    
    @Override
    public OrderQueryResult updateOrderStatus(UpdateOrderStatusCommand command) {
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
        
        Order savedOrder = orderRepository.save(order);
        return toOrderQueryResult(savedOrder);
    }
    
    private OrderQueryResult toOrderQueryResult(Order order) {
        List<OrderItemQueryResult> items = order.getItems().stream()
            .map(item -> new OrderItemQueryResult(
                item.getProductId().getValue(),
                item.getUnitPrice().getAmount(),
                item.getQuantity().getValue(),
                item.calculateTotal().getAmount()
            ))
            .toList();
        
        return new OrderQueryResult(
            order.getId().getValue(),
            order.getStatus().name(),
            order.calculateTotal().getAmount(),
            order.getCreatedAt(),
            items
        );
    }
}
