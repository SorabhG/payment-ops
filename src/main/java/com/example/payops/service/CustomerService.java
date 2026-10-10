
package com.example.payops.service;

import com.example.payops.dto.CreateCustomerRequest;
import com.example.payops.entity.Customer;
import com.example.payops.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Contains customer business logic and database operations. */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    /** Spring injects the repository through the constructor. */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /** Creates a customer after checking that the email is unused. */
    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerEmailAlreadyExistsException(request.email());
        }

        Customer customer = new Customer(
                request.name(),
                request.email()
        );

        return customerRepository.save(customer);
    }

    /** Returns a customer or raises a specific not-found exception. */
    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
