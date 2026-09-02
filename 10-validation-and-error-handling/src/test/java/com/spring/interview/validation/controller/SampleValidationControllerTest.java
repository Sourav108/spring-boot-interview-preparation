package com.spring.interview.validation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.interview.validation.dto.CustomerRegistrationDto;
import com.spring.interview.validation.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SampleValidationController.class)
@Import(GlobalExceptionHandler.class)
class SampleValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should accept valid customer registration payload with valid E.164 phone number")
    void shouldRegisterValidCustomer() throws Exception {
        var dto = new CustomerRegistrationDto("Jane Doe", "jane.doe@example.com", "+14155552671");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("REGISTERED"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request ProblemDetail when phone number fails E.164 format constraint")
    void shouldRejectInvalidPhoneNumber() throws Exception {
        var dto = new CustomerRegistrationDto("Jane Doe", "jane.doe@example.com", "not-a-phone-number");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string("Content-Type", "application/problem+json"))
            .andExpect(jsonPath("$.title").value("Validation Failure"))
            .andExpect(jsonPath("$.invalidParams").isArray())
            .andExpect(jsonPath("$.invalidParams[0].field").value("phoneNumber"));
    }

    @Test
    @DisplayName("Should return 404 Not Found ProblemDetail for missing resource")
    void shouldReturn404ProblemDetail() throws Exception {
        mockMvc.perform(get("/api/customers/missing"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Resource Not Found"))
            .andExpect(jsonPath("$.detail").value("Customer with id 'missing' not found"));
    }

    @Test
    @DisplayName("Should return 409 Conflict ProblemDetail on duplicate customer email")
    void shouldReturn409ProblemDetail() throws Exception {
        var dto = new CustomerRegistrationDto("Jane Doe", "existing@example.com", "+14155552671");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.title").value("Resource Conflict"));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error with sanitized payload and correlation reference ID")
    void shouldReturnSanitized500ProblemDetail() throws Exception {
        mockMvc.perform(get("/api/customers/crash"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.title").value("Internal Server Error"))
            .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("Please quote reference:")))
            .andExpect(jsonPath("$.errorReferenceId").isNotEmpty())
            // Guarantee internal raw stack trace message is NOT leaked to caller
            .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Simulated unexpected database"))));
    }
}
