package com.spring.interview.mvc.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AuditLoggingFilterTest {

    @Test
    @DisplayName("Should execute filter chain and attach X-Response-Time-Millis header")
    void shouldAppendResponseTimeHeader() throws Exception {
        AuditLoggingFilter filter = new AuditLoggingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain mockChain = (req, res) -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        };

        filter.doFilter(request, response, mockChain);

        String responseTime = response.getHeader("X-Response-Time-Millis");
        assertThat(responseTime).isNotNull();
        assertThat(Long.parseLong(responseTime)).isGreaterThanOrEqualTo(5L);
    }
}
