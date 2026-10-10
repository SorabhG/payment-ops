package com.example.payops.controller;

import com.example.payops.dto.CreatePaymentRequest;
import com.example.payops.dto.PaymentResponse;
import com.example.payops.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Exposes REST endpoints for creating and retrieving payments.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Constructs the controller with the payment service.
     *
     * @param paymentService payment business logic
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a payment for an existing order.
     *
     * @param request validated payment details
     * @return the newly created payment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        return PaymentResponse.from(paymentService.createPayment(request));
    }

    /**
     * Retrieves a payment by its UUID.
     *
     * @param id payment UUID from the URL
     * @return the requested payment
     */
    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable UUID id) {
        return PaymentResponse.from(paymentService.getPaymentById(id));
    }
}