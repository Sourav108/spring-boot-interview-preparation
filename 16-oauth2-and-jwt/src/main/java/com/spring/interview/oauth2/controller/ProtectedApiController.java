package com.spring.interview.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProtectedApiController {

    @GetMapping("/public/info")
    public Map<String, String> publicInfo() {
        return Map.of("service", "Order Microservice", "status", "HEALTHY");
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrders(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "userId", jwt.getSubject(),
            "username", jwt.getClaimAsString("preferred_username") != null ? jwt.getClaimAsString("preferred_username") : jwt.getSubject(),
            "orders", List.of(
                Map.of("id", "ORD-101", "total", 149.99),
                Map.of("id", "ORD-102", "total", 299.50)
            )
        );
    }

    @GetMapping("/admin/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> adminMetrics() {
        return Map.of("activeUsers", 1420, "systemLoad", "0.42");
    }
}
