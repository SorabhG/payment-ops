
package com.example.payops.service;

import com.example.payops.dto.CreateOrderRequest;
import com.example.payops.entity.Customer;
import com.example.payops.entity.Order;
import com.example.payops.exception.CustomerNotFoundException;
import com.example.payops.exception.OrderNotFoundException;
import com.example.payops.repository.CustomerRepository;
import com.example.payops.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.customerId()));

        Order order = new Order(
                customer,
                request.orderReference(),
                request.amount(),
                request.currency());

        return orderRepository.save(order);
    }
}