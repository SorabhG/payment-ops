package com.example.payops.service;

import com.example.payops.dto.CreatePaymentRequest;
import com.example.payops.entity.Order;
import com.example.payops.entity.Payment;
import com.example.payops.exception.OrderNotFoundException;
import com.example.payops.exception.PaymentNotFoundException;
import com.example.payops.repository.OrderRepository;
import com.example.payops.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implements payment creation and retrieval business logic.
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    /**
     * Constructs the service with its required repositories.
     *
     * @param paymentRepository repository for payment persistence
     * @param orderRepository repository for order lookup
     */
    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves a payment by its UUID.
     *
     * @param id the payment UUID
     * @return the existing payment
     * @throws PaymentNotFoundException if the payment does not exist
     */
    @Transactional(readOnly = true)
    public Payment getPaymentById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

/**
 * Creates a payment for an existing order without allowing overpayment.
 *
 * Existing payments, including PENDING payments, count toward the order
 * total to prevent concurrent requests from being treated as free capacity.
 *
 * @param request validated payment creation details
 * @return the saved payment
 * @throws OrderNotFoundException if the referenced order does not exist
 * @throws IllegalArgumentException if currency differs or the order
 *         would be overpaid
 */
@Transactional
public Payment createPayment(CreatePaymentRequest request) {

    Order order = orderRepository.findById(request.orderId())
            .orElseThrow(
                    () -> new OrderNotFoundException(request.orderId()));

    // Payment and order must use the same currency.
    if (!order.getCurrency().equals(request.currency())) {
        throw new IllegalArgumentException(
                "Payment currency must match order currency: "
                        + order.getCurrency());
    }

    // Count existing payments before accepting another payment.
    List<Payment> existingPayments =
            paymentRepository.findByOrderId(request.orderId());

    BigDecimal alreadyPaidOrPending = existingPayments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalAfterPayment =
            alreadyPaidOrPending.add(request.amount());

    // Reject a payment that would exceed the order total.
    if (totalAfterPayment.compareTo(order.getAmount()) > 0) {
        throw new IllegalArgumentException(
                "Payment would exceed order amount. Order amount: "
                        + order.getAmount()
                        + ", existing payments: " + alreadyPaidOrPending
                        + ", requested payment: " + request.amount());
    }

    Payment payment = new Payment(
            order,
            request.amount(),
            request.currency());

    return paymentRepository.save(payment);
}
}