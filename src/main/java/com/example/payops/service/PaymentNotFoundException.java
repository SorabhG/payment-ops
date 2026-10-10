package com.example.payops.service;

import java.util.UUID;

/**
 * Exception thrown when a requested payment does not exist.
 */
public class PaymentNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a missing payment.
     *
     * @param id the payment UUID that could not be found
     */
    public PaymentNotFoundException(UUID id) {
        super("Payment not found: " + id);
    }
}