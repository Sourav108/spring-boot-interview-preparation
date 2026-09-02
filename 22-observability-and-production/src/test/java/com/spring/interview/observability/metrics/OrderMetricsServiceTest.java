package com.spring.interview.observability.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMetricsServiceTest {

    private final SimpleMeterRegistry registry = new SimpleMeterRegistry();
    private final OrderMetricsService metricsService = new OrderMetricsService(registry);

    @Test
    @DisplayName("Should increment success and failure counters and record duration in timer")
    void shouldRecordMetricsCorrectly() {
        metricsService.recordOrder(true, 120);
        metricsService.recordOrder(true, 85);
        metricsService.recordOrder(false, 300);

        assertThat(metricsService.getSuccessCount()).isEqualTo(2.0);
        assertThat(metricsService.getFailureCount()).isEqualTo(1.0);
        assertThat(metricsService.getTotalTimedCount()).isEqualTo(3);
    }
}
