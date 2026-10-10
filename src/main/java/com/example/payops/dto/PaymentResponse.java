package com.example.payops.dto;

import com.example.payops.entity.Payment;
import com.example.payops.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payload returned when a payment is created or retrieved.
 */
public record PaymentResponse(
        UUID id,
        UUID orderId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    /**
     * Converts a Payment entity into its API response representation.
     *
     * @param payment the payment entity
     * @return the payment response DTO
     */
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}