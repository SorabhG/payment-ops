
package com.example.payops.controller;

import com.example.payops.dto.CreateCustomerRequest;
import com.example.payops.dto.CustomerResponse;
import com.example.payops.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Exposes HTTP endpoints for creating and retrieving customers. */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    /** Spring injects the customer service automatically. */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** Creates a customer and returns HTTP 201 Created. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        return CustomerResponse.from(
                customerService.createCustomer(request));
    }

    /** Retrieves a customer by UUID, returning 404 if absent. */
    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable UUID id) {
        return CustomerResponse.from(
                customerService.getCustomerById(id));
    }
}
