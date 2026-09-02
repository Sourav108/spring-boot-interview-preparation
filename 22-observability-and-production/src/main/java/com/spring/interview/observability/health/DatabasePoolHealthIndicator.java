package com.spring.interview.observability.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom HealthIndicator evaluating connection pool saturation for Kubernetes readiness probes.
 */
@Component
public class DatabasePoolHealthIndicator implements HealthIndicator {

    private final AtomicInteger activeConnections = new AtomicInteger(4);
    private final int maxConnections = 20;

    @Override
    public Health health() {
        int current = activeConnections.get();
        double saturation = (double) current / maxConnections;

        if (saturation >= 0.95) {
            return Health.down()
                .withDetail("activeConnections", current)
                .withDetail("maxConnections", maxConnections)
                .withDetail("reason", "Connection pool near exhaustion (>95% saturation)")
                .build();
        }

        return Health.up()
            .withDetail("activeConnections", current)
            .withDetail("maxConnections", maxConnections)
            .withDetail("saturationPercentage", (int) (saturation * 100))
            .build();
    }

    public void setActiveConnections(int count) {
        activeConnections.set(count);
    }
}
