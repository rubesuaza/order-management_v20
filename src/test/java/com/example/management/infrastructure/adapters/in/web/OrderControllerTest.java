package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.CreateOrderCommand;
import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.in.UpdateOrderStatusCommand;
import com.example.management.application.ports.in.UpdateOrderStatusUseCase;
import com.example.management.application.services.dto.OrderItemQueryResult;
import com.example.management.application.services.dto.OrderQueryResult;
import com.example.management.domain.model.OrderId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de contrato para el REST Controller de órdenes.
 * Verifica que el controlador maneje correctamente las peticiones HTTP.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CreateOrderUseCase createOrderUseCase;
    
    @MockBean
    private GetOrderUseCase getOrderUseCase;
    
    @MockBean
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;
    
    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        OrderQueryResult orderResult = new OrderQueryResult(
            "ORDER-001",
            "PENDING",
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            List.of(new OrderItemQueryResult(
                "PROD-001",
                BigDecimal.valueOf(10.0),
                2,
                BigDecimal.valueOf(20.0)
            ))
        );
        
        when(createOrderUseCase.createOrder(any(CreateOrderCommand.class)))
            .thenReturn(orderResult);
        
        String requestBody = """
            {
                "orderId": "ORDER-001",
                "items": [
                    {
                        "productId": "PROD-001",
                        "unitPrice": 10.0,
                        "quantity": 2
                    }
                ]
            }
            """;
        
        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("ORDER-001"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.total").value(20.0))
            .andExpect(jsonPath("$.items[0].productId").value("PROD-001"))
            .andExpect(jsonPath("$.items[0].quantity").value(2));
    }
    
    @Test
    void shouldReturnBadRequestWhenCreatingOrderWithInvalidData() throws Exception {
        // Given
        String requestBody = """
            {
                "orderId": "",
                "items": []
            }
            """;
        
        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldGetOrder() throws Exception {
        // Given
        OrderId orderId = new OrderId("ORDER-001");
        OrderQueryResult orderResult = new OrderQueryResult(
            "ORDER-001",
            "PENDING",
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            List.of(new OrderItemQueryResult(
                "PROD-001",
                BigDecimal.valueOf(10.0),
                2,
                BigDecimal.valueOf(20.0)
            ))
        );
        
        when(getOrderUseCase.getOrder(orderId))
            .thenReturn(Optional.of(orderResult));
        
        // When & Then
        mockMvc.perform(get("/api/orders/ORDER-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("ORDER-001"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.total").value(20.0));
    }
    
    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        // Given
        OrderId orderId = new OrderId("ORDER-NOT-FOUND");
        
        when(getOrderUseCase.getOrder(orderId))
            .thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/orders/ORDER-NOT-FOUND"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldUpdateOrderStatus() throws Exception {
        // Given
        OrderQueryResult orderResult = new OrderQueryResult(
            "ORDER-001",
            "CONFIRMED",
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            List.of(new OrderItemQueryResult(
                "PROD-001",
                BigDecimal.valueOf(10.0),
                2,
                BigDecimal.valueOf(20.0)
            ))
        );
        
        when(updateOrderStatusUseCase.updateOrderStatus(any(UpdateOrderStatusCommand.class)))
            .thenReturn(orderResult);
        
        String requestBody = """
            {
                "status": "CONFIRMED"
            }
            """;
        
        // When & Then
        mockMvc.perform(patch("/api/orders/ORDER-001/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("ORDER-001"))
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
    
    @Test
    void shouldReturnBadRequestWhenUpdatingWithInvalidStatus() throws Exception {
        // Given
        String requestBody = """
            {
                "status": "INVALID_STATUS"
            }
            """;
        
        // When & Then
        mockMvc.perform(patch("/api/orders/ORDER-001/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }
}
