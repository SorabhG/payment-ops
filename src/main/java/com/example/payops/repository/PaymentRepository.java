package com.example.payops.repository;

import com.example.payops.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Provides database operations for Payment entities.
 */
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Retrieves all payments associated with an order.
     *
     * @param orderId the order UUID
     * @return payments belonging to the order
     */
    List<Payment> findByOrderId(UUID orderId);
}