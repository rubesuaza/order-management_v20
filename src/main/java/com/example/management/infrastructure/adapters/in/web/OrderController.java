package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.Quantity;
import com.example.management.infrastructure.adapters.in.web.dto.CreateOrderRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderResponse;
import com.example.management.infrastructure.adapters.in.web.dto.UpdateOrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de órdenes.
 * Adaptador de entrada que expone los casos de uso a través de HTTP.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    
    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderId orderId = new OrderId(request.orderId());
        List<OrderItem> items = request.items().stream()
            .map(item -> new OrderItem(
                item.productId(),
                new Money(item.unitPrice()),
                new Quantity(item.quantity())
            ))
            .collect(Collectors.toList());
        
        Order order = createOrderUseCase.createOrder(orderId, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(new OrderId(orderId))
            .map(order -> ResponseEntity.ok(OrderResponse.from(order)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(request.status().toUpperCase());
            Order updatedOrder = updateOrderStatusUseCase.updateOrderStatus(new OrderId(orderId), newStatus);
            return ResponseEntity.ok(OrderResponse.from(updatedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
