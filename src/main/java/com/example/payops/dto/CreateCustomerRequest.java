
package com.example.payops.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Validates the data accepted when a client creates a customer. */
public record CreateCustomerRequest(
        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Email
        @Size(max = 254)
        String email
) {}
