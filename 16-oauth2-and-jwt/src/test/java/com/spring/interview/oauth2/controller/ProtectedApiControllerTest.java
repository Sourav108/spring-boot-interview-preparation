package com.spring.interview.oauth2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProtectedApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should permit anonymous access to public info endpoint")
    void shouldAllowPublicInfo() throws Exception {
        mockMvc.perform(get("/api/v1/public/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("Order Microservice"));
    }

    @Test
    @DisplayName("Should reject unauthenticated access to /orders with 401 Unauthorized")
    void shouldRejectUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should grant access to /orders when authenticated with valid user JWT")
    void shouldAllowAuthenticatedJwtUser() throws Exception {
        mockMvc.perform(get("/api/v1/orders")
            .with(jwt()
                .jwt(builder -> builder
                    .subject("usr-uuid-123")
                    .claim("preferred_username", "sourav")
                )
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
            ))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value("usr-uuid-123"))
            .andExpect(jsonPath("$.username").value("sourav"))
            .andExpect(jsonPath("$.orders").isArray());
    }

    @Test
    @DisplayName("Should forbid standard USER role from accessing admin metrics endpoint (403 Forbidden)")
    void shouldForbidNonAdminUser() throws Exception {
        mockMvc.perform(get("/api/v1/admin/metrics")
            .with(jwt()
                .jwt(builder -> builder
                    .subject("usr-uuid-123")
                    .claim("preferred_username", "sourav")
                )
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
            ))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should grant access to admin metrics endpoint when JWT contains ADMIN role")
    void shouldAllowAdminJwt() throws Exception {
        mockMvc.perform(get("/api/v1/admin/metrics")
            .with(jwt()
                .jwt(builder -> builder
                    .subject("admin-uuid-999")
                    .claim("preferred_username", "security-admin")
                )
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"))
            ))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activeUsers").value(1420));
    }
}
