
package com.example.payops.dto;

import com.example.payops.entity.Customer;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * API representation of a customer, separate from the JPA entity.
 */
public record CustomerResponse(
        UUID id,
        String name,
        String email,
        OffsetDateTime createdAt
) {
    /** Maps a persisted Customer entity to the API response. */
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }
}
