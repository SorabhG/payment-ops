
package com.example.payops.dto;

import com.example.payops.entity.Refund;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response containing refund details.
 */
public record RefundResponse(
        UUID id,
        UUID paymentId,
        BigDecimal amount,
        String currency,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    /**
     * Maps a refund entity to its API response.
     *
     * @param refund the refund entity
     * @return the response DTO
     */
    public static RefundResponse from(Refund refund) {
        return new RefundResponse(
                refund.getId(),
                refund.getPayment().getId(),
                refund.getAmount(),
                refund.getCurrency(),
                refund.getStatus().name(),
                refund.getCreatedAt(),
                refund.getUpdatedAt()
        );
    }
}
