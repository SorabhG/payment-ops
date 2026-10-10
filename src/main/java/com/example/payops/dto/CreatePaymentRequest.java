package com.example.payops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request payload used to create a payment for an order.
 */
public record CreatePaymentRequest(

        @NotNull
        UUID orderId,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,

        @NotBlank
        @Pattern(regexp = "[A-Z]{3}")
        String currency

) {
}