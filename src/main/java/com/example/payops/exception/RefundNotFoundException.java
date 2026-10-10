
package com.example.payops.exception;

import java.util.UUID;

/**
 * Indicates that the requested refund does not exist.
 */
public class RefundNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a missing refund.
     *
     * @param id the refund identifier
     */
    public RefundNotFoundException(UUID id) {
        super("Refund not found: " + id);
    }
}
