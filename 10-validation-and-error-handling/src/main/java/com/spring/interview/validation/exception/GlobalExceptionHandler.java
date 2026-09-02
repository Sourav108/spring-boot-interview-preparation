package com.spring.interview.validation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Enterprise Global Exception Handler translating exceptions into RFC 7807 Problem Details
 * while strictly sanitizing database and stack trace details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public record ValidationErrorItem(String field, String reason) {}

    // 1. Validation Failures (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Request validation failed. Please correct the invalid fields."
        );
        problem.setTitle("Validation Failure");
        problem.setType(URI.create("https://api.example.com/errors/validation-failure"));

        List<ValidationErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> new ValidationErrorItem(err.getField(), err.getDefaultMessage()))
            .toList();

        problem.setProperty("invalidParams", errors);
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    // 2. Resource Not Found (404 Not Found)
    @ExceptionHandler(DomainExceptions.ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(DomainExceptions.ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setType(URI.create("https://api.example.com/errors/not-found"));
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    // 3. Business Conflict (409 Conflict)
    @ExceptionHandler(DomainExceptions.BusinessConflictException.class)
    public ProblemDetail handleConflict(DomainExceptions.BusinessConflictException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Resource Conflict");
        problem.setType(URI.create("https://api.example.com/errors/conflict"));
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    // 4. Unexpected Bug / Internal Fallback (500 Internal Server Error) - SANITIZED
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        String errorReferenceId = UUID.randomUUID().toString();
        // Log full raw stack trace internally
        log.error("Unhandled internal exception [Ref: {}]", errorReferenceId, ex);

        // Sanitize response to external caller
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected internal error occurred. Please quote reference: " + errorReferenceId
        );
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://api.example.com/errors/internal-error"));
        problem.setProperty("errorReferenceId", errorReferenceId);
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }
}
