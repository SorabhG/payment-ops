
package com.example.payops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request to create a refund against an existing payment.
 */
public record CreateRefundRequest(
        @NotNull UUID paymentId,
        @NotNull @DecimalMin("0.01") BigDecimal amount) {
}
