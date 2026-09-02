package com.spring.interview.mvc.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RequestCorrelationInterceptorTest {

    @Test
    @DisplayName("Should generate new correlation ID if absent in request header")
    void shouldGenerateCorrelationIdWhenMissing() {
        RequestCorrelationInterceptor interceptor = new RequestCorrelationInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(response.getHeader(RequestCorrelationInterceptor.CORRELATION_ID_HEADER)).isNotNull();
        assertThat(request.getAttribute(RequestCorrelationInterceptor.CORRELATION_ID_HEADER)).isNotNull();
    }

    @Test
    @DisplayName("Should preserve existing correlation ID from request header")
    void shouldPreserveExistingCorrelationId() {
        RequestCorrelationInterceptor interceptor = new RequestCorrelationInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestCorrelationInterceptor.CORRELATION_ID_HEADER, "custom-trace-12345");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(response.getHeader(RequestCorrelationInterceptor.CORRELATION_ID_HEADER)).isEqualTo("custom-trace-12345");
    }
}
