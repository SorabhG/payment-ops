
package com.example.payops.repository;

import com.example.payops.entity.Order;
import com.example.payops.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<Order, UUID> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomerId(UUID customerId);
}