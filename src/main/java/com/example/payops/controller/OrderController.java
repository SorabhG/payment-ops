package com.example.payops.controller;

import com.example.payops.dto.CreateOrderRequest;
import com.example.payops.dto.OrderResponse;
import com.example.payops.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Retrieves an existing order using its UUID.
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable UUID id) {
        return OrderResponse.from(orderService.getOrderById(id));
    }

    // Creates an order for an existing customer.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return OrderResponse.from(orderService.createOrder(request));
    }
}

