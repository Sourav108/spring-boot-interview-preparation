package com.spring.interview.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.interview.rest.dto.UserRequestDto;
import com.spring.interview.rest.service.InMemoryUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@Import(InMemoryUserService.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InMemoryUserService userService;

    @BeforeEach
    void setUp() {
        userService.clear();
    }

    @Test
    @DisplayName("Should execute complete REST CRUD lifecycle: POST -> GET -> PUT -> DELETE")
    void shouldExecuteRestCrudLifecycle() throws Exception {
        // 1. POST /api/v1/users (Create)
        UserRequestDto createRequest = new UserRequestDto("sourav", "sourav@example.com", "ADMIN");
        String createJson = objectMapper.writeValueAsString(createRequest);

        String responseContent = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.username").value("sourav"))
            .andExpect(jsonPath("$.email").value("sourav@example.com"))
            .andReturn().getResponse().getContentAsString();

        String userId = objectMapper.readTree(responseContent).get("id").asText();

        // 2. GET /api/v1/users/{id} (Retrieve by ID)
        mockMvc.perform(get("/api/v1/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.username").value("sourav"));

        // 3. GET /api/v1/users (Paginated List)
        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

        // 4. PUT /api/v1/users/{id} (Update)
        UserRequestDto updateRequest = new UserRequestDto("sourav_updated", "sourav.new@example.com", "SUPER_ADMIN");
        mockMvc.perform(put("/api/v1/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("sourav_updated"))
            .andExpect(jsonPath("$.role").value("SUPER_ADMIN"));

        // 5. DELETE /api/v1/users/{id} (Delete)
        mockMvc.perform(delete("/api/v1/users/" + userId))
            .andExpect(status().isNoContent());

        // 6. Verify 404 on subsequent GET
        mockMvc.perform(get("/api/v1/users/" + userId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when validation constraints are violated")
    void shouldReturn400OnInvalidPayload() throws Exception {
        UserRequestDto invalidRequest = new UserRequestDto("", "invalid-email", "");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }
}
