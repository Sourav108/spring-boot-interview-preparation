package com.spring.interview.cloud.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign RequestInterceptor ensuring distributed tracing correlation IDs are propagated downstream.
 */
@Component
public class FeignCorrelationIdInterceptor implements RequestInterceptor {

    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    @Override
    public void apply(RequestTemplate template) {
        if (!template.headers().containsKey(CORRELATION_HEADER)) {
            template.header(CORRELATION_HEADER, "trace-auto-" + System.currentTimeMillis());
        }
    }
}
