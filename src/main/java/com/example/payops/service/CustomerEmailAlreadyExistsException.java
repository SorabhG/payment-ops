
package com.example.payops.service;

/** Identifies an attempt to register an email already in use. */
public class CustomerEmailAlreadyExistsException extends RuntimeException {

    public CustomerEmailAlreadyExistsException(String email) {
        super("A customer with email " + email + " already exists");
    }
}
