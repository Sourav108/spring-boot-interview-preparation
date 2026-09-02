package com.spring.interview.security.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecuredResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should permit anonymous access to public endpoint")
    void shouldAllowAnonymousAccessToPublicEndpoint() throws Exception {
        mockMvc.perform(get("/api/public/hello"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access").value("ANONYMOUS"));
    }

    @Test
    @DisplayName("Should reject unauthenticated access to protected user endpoint with 401 Unauthorized")
    void shouldDenyUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/protected/user"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "alice", roles = {"USER"})
    @DisplayName("Should grant access to user endpoint for authenticated user")
    void shouldAllowAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/protected/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    @WithMockUser(username = "alice", roles = {"USER"})
    @DisplayName("Should forbid access to admin endpoint for regular user (403 Forbidden)")
    void shouldForbidUserFromAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/protected/admin"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "superadmin", roles = {"ADMIN"})
    @DisplayName("Should grant access to admin endpoint for ADMIN role via @PreAuthorize")
    void shouldAllowAdminRole() throws Exception {
        mockMvc.perform(get("/api/protected/admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.adminUser").value("superadmin"));
    }
}
