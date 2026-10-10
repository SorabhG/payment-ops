
package com.example.payops.repository;

import com.example.payops.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Provides database access for refund records.
 */
public interface RefundRepository extends JpaRepository<Refund, UUID> {

    /**
     * Finds all refunds associated with a payment.
     *
     * @param paymentId the payment identifier
     * @return refunds associated with the payment
     */
    List<Refund> findByPayment_Id(UUID paymentId);
}
