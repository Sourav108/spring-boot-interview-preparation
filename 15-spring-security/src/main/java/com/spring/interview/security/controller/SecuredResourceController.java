package com.spring.interview.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecuredResourceController {

    @GetMapping("/public/hello")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "Welcome to Public API", "access", "ANONYMOUS");
    }

    @GetMapping("/protected/user")
    public Map<String, Object> userEndpoint(Authentication authentication) {
        return Map.of(
            "message", "Welcome User",
            "username", authentication.getName(),
            "authorities", authentication.getAuthorities().stream().map(Object::toString).toList()
        );
    }

    @GetMapping("/protected/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminEndpoint(Authentication authentication) {
        return Map.of(
            "message", "Admin Privileged Access Granted",
            "adminUser", authentication.getName()
        );
    }
}
