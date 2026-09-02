package com.spring.interview.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service demonstrating Micrometer Counter and Timer metric recording.
 */
@Service
public class OrderMetricsService {

    private final Counter orderSuccessCounter;
    private final Counter orderFailureCounter;
    private final Timer orderProcessingTimer;

    public OrderMetricsService(MeterRegistry registry) {
        this.orderSuccessCounter = Counter.builder("orders.placed")
            .tag("status", "success")
            .description("Total number of successfully placed orders")
            .register(registry);

        this.orderFailureCounter = Counter.builder("orders.placed")
            .tag("status", "failed")
            .description("Total number of failed order attempts")
            .register(registry);

        this.orderProcessingTimer = Timer.builder("orders.processing.duration")
            .description("Time taken to process and execute order checkout")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }

    public void recordOrder(boolean success, long durationMillis) {
        if (success) {
            orderSuccessCounter.increment();
        } else {
            orderFailureCounter.increment();
        }
        orderProcessingTimer.record(durationMillis, TimeUnit.MILLISECONDS);
    }

    public double getSuccessCount() { return orderSuccessCounter.count(); }
    public double getFailureCount() { return orderFailureCounter.count(); }
    public long getTotalTimedCount() { return orderProcessingTimer.count(); }
}
