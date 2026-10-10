package com.example.payops.controller;

import com.example.payops.service.CustomerNotFoundException;
import com.example.payops.service.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.payops.service.CustomerEmailAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ApiError(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            String path) {
    }

    private ResponseEntity<ApiError> response(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ResponseEntity.status(status).body(new ApiError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> orderNotFound(
            OrderNotFoundException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> customerNotFound(
            CustomerNotFoundException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validationFailed(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed");

        return response(HttpStatus.BAD_REQUEST, message, request);
    }

    /** Returns HTTP 409 when an email is already registered. */
    @ExceptionHandler(CustomerEmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> duplicateCustomerEmail(
            CustomerEmailAlreadyExistsException ex,
            HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /** Handles database constraint violations as a conflict response. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> dataConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return response(
                HttpStatus.CONFLICT,
                "The request conflicts with existing data",
                request);
    }

    /**
     * Handles invalid business requests, such as currency mismatches
     * and payments that exceed the order amount.
     *
     * @param exception the business validation exception
     * @param request   the HTTP request that caused the exception
     * @return a 400 Bad Request response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.badRequest().body(error);
    }

}