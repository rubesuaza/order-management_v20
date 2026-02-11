package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.CreateOrderCommand;
import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusCommand;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.application.services.dto.OrderQueryResult;
import com.example.management.domain.model.OrderId;
import com.example.management.infrastructure.adapters.in.web.dto.CreateOrderRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderResponse;
import com.example.management.infrastructure.adapters.in.web.dto.UpdateOrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
        CreateOrderCommand command = new CreateOrderCommand(
            request.orderId(),
            request.items().stream()
                .map(item -> new CreateOrderCommand.OrderItemCommand(
                    item.productId(),
                    BigDecimal.valueOf(item.unitPrice()),
                    item.quantity()
                ))
                .toList()
        );
        
        OrderQueryResult orderResult = createOrderUseCase.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(orderResult));
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(new OrderId(orderId))
            .map(OrderResponse::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        try {
            UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(orderId, request.status());
            OrderQueryResult updatedOrderResult = updateOrderStatusUseCase.updateOrderStatus(command);
            return ResponseEntity.ok(OrderResponse.from(updatedOrderResult));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
