package com.spring.interview.cloud.interceptor;

import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeignCorrelationIdInterceptorTest {

    private final FeignCorrelationIdInterceptor interceptor = new FeignCorrelationIdInterceptor();

    @Test
    @DisplayName("Should inject X-Correlation-Id header into outgoing Feign request template")
    void shouldInjectCorrelationIdHeader() {
        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertThat(template.headers()).containsKey(FeignCorrelationIdInterceptor.CORRELATION_HEADER);
        var headerValues = template.headers().get(FeignCorrelationIdInterceptor.CORRELATION_HEADER);
        assertThat(headerValues).isNotEmpty();
        assertThat(headerValues.iterator().next()).startsWith("trace-auto-");
    }
}
