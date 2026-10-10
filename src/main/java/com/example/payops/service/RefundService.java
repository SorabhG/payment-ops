
package com.example.payops.service;

import com.example.payops.dto.CreateRefundRequest;
import com.example.payops.dto.RefundResponse;
import com.example.payops.entity.Payment;
import com.example.payops.entity.Refund;
import com.example.payops.entity.RefundStatus;
import com.example.payops.exception.PaymentNotFoundException;
import com.example.payops.exception.RefundNotFoundException;
import com.example.payops.repository.PaymentRepository;
import com.example.payops.repository.RefundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implements refund creation and retrieval business logic.
 */
@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Creates the service with its required repositories.
     *
     * @param refundRepository repository for refunds
     * @param paymentRepository repository for payments
     */
    public RefundService(
            RefundRepository refundRepository,
            PaymentRepository paymentRepository) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Creates a refund while preventing the total non-failed refunds
     * from exceeding the original payment amount.
     *
     * @param request the refund request
     * @return the created refund
     */
    @Transactional
    public RefundResponse createRefund(CreateRefundRequest request) {
        Payment payment = paymentRepository.findById(request.paymentId())
                .orElseThrow(
                        () -> new PaymentNotFoundException(request.paymentId()));

        List<Refund> existingRefunds =
                refundRepository.findByPayment_Id(payment.getId());

        BigDecimal alreadyRefunded = existingRefunds.stream()
                .filter(refund -> refund.getStatus() != RefundStatus.FAILED)
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal requestedAmount = request.amount();
        BigDecimal availableAmount =
                payment.getAmount().subtract(alreadyRefunded);

        if (requestedAmount.compareTo(availableAmount) > 0) {
            throw new IllegalArgumentException(
                    "Refund would exceed payment amount. Payment amount: "
                            + payment.getAmount()
                            + ", existing non-failed refunds: "
                            + alreadyRefunded
                            + ", requested refund: "
                            + requestedAmount);
        }

        Refund refund = new Refund(
                payment,
                requestedAmount,
                payment.getCurrency());

        Refund savedRefund = refundRepository.save(refund);

        return RefundResponse.from(savedRefund);
    }

    /**
     * Retrieves a refund by its identifier.
     *
     * @param id the refund identifier
     * @return the refund response
     */
    @Transactional(readOnly = true)
    public RefundResponse getRefundById(UUID id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RefundNotFoundException(id));

        return RefundResponse.from(refund);
    }
}
