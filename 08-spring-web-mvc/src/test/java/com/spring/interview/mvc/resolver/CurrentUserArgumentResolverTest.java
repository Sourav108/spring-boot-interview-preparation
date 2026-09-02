package com.spring.interview.mvc.resolver;

import com.spring.interview.mvc.config.WebMvcCustomConfiguration;
import com.spring.interview.mvc.controller.SampleProfileController;
import com.spring.interview.mvc.interceptor.RequestCorrelationInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SampleProfileController.class)
@Import({WebMvcCustomConfiguration.class, RequestCorrelationInterceptor.class})
class CurrentUserArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should resolve @CurrentUser from custom HTTP headers")
    void shouldResolveCustomUserPrincipalFromHeaders() throws Exception {
        mockMvc.perform(get("/api/profiles/me")
                .header("X-User-Id", "usr-889")
                .header("X-User-Email", "lead.engineer@company.com")
                .header("X-User-Role", "ROLE_LEAD"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value("usr-889"))
            .andExpect(jsonPath("$.email").value("lead.engineer@company.com"))
            .andExpect(jsonPath("$.role").value("ROLE_LEAD"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should provide anonymous fallback when auth headers are absent")
    void shouldProvideAnonymousFallback() throws Exception {
        mockMvc.perform(get("/api/profiles/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value("anonymous"))
            .andExpect(jsonPath("$.role").value("ROLE_ANONYMOUS"));
    }
}
