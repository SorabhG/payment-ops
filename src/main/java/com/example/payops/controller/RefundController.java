
package com.example.payops.controller;

import com.example.payops.dto.CreateRefundRequest;
import com.example.payops.dto.RefundResponse;
import com.example.payops.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST endpoints for creating and retrieving refunds.
 */
@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    /**
     * Creates the controller with its service dependency.
     *
     * @param refundService refund business logic
     */
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    /**
     * Creates a refund.
     *
     * @param request the refund request
     * @return the created refund with HTTP 201
     */
    @PostMapping
    public ResponseEntity<RefundResponse> createRefund(
            @Valid @RequestBody CreateRefundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refundService.createRefund(request));
    }

    /**
     * Retrieves a refund by identifier.
     *
     * @param id the refund identifier
     * @return the refund with HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<RefundResponse> getRefundById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }
}
