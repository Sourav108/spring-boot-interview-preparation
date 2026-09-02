package com.spring.interview.validation.controller;

import com.spring.interview.validation.dto.CustomerRegistrationDto;
import com.spring.interview.validation.exception.DomainExceptions;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class SampleValidationController {

    @PostMapping
    public ResponseEntity<Map<String, String>> registerCustomer(@Valid @RequestBody CustomerRegistrationDto request) {
        if ("existing@example.com".equalsIgnoreCase(request.email())) {
            throw new DomainExceptions.BusinessConflictException("Customer with email " + request.email() + " already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "REGISTERED", "email", request.email()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getCustomer(@PathVariable String id) {
        if ("missing".equalsIgnoreCase(id)) {
            throw new DomainExceptions.ResourceNotFoundException("Customer with id '" + id + "' not found");
        }
        if ("crash".equalsIgnoreCase(id)) {
            throw new RuntimeException("Simulated unexpected database connection failure");
        }
        return ResponseEntity.ok(Map.of("id", id, "name", "Jane Doe"));
    }
}
