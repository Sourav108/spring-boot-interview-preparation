package com.spring.interview.observability.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;

class DatabasePoolHealthIndicatorTest {

    private final DatabasePoolHealthIndicator healthIndicator = new DatabasePoolHealthIndicator();

    @Test
    @DisplayName("Should report UP status when pool active connections are within safe limits")
    void shouldReportUpWhenPoolHealthy() {
        healthIndicator.setActiveConnections(10); // 50% saturation
        var health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("saturationPercentage", 50);
    }

    @Test
    @DisplayName("Should report DOWN status when pool active connections exceed 95% threshold")
    void shouldReportDownWhenPoolExhausted() {
        healthIndicator.setActiveConnections(20); // 100% saturation
        var health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("reason");
    }
}
