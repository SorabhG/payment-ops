package com.example.payops.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "payment_attempts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_payment_attempt_number",
            columnNames = {"payment_id", "attempt_number"}
        )
    },
    indexes = {
        @Index(
            name = "idx_payment_attempts_payment",
            columnList = "payment_id, attempt_number DESC"
        )
    }
)
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentAttemptStatus status;

    @Column(name = "provider_reference", length = 150)
    private String providerReference;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected PaymentAttempt() {
    }

    public PaymentAttempt(Payment payment, Integer attemptNumber) {
        this.payment = payment;
        this.attemptNumber = attemptNumber;
        this.status = PaymentAttemptStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public PaymentAttemptStatus getStatus() {
        return status;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}