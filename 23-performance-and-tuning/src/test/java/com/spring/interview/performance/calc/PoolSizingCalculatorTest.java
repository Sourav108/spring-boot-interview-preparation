package com.spring.interview.performance.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PoolSizingCalculatorTest {

    private final PoolSizingCalculator calculator = new PoolSizingCalculator();

    @Test
    @DisplayName("Should correctly calculate pool recommendation for 8 DB cores and 4 pods")
    void shouldCalculatePoolSizeForStandardCluster() {
        var spec = new PoolSizingCalculator.HardwareSpec(8, 1, 4);
        var recommendation = calculator.calculateRecommendedPoolSize(spec);

        // Optimal total connections = (8 * 2) + 1 = 17
        // Across 4 pods = 17 / 4 = 4 connections per pod
        assertThat(recommendation.connectionsPerPod()).isEqualTo(4);
        assertThat(recommendation.totalClusterConnections()).isEqualTo(16);
        assertThat(recommendation.sizingRationale()).contains("optimal DB connections = 17");
    }

    @Test
    @DisplayName("Should enforce minimum connections per pod when pod count is high")
    void shouldEnforceMinimumConnections() {
        var spec = new PoolSizingCalculator.HardwareSpec(4, 1, 10);
        var recommendation = calculator.calculateRecommendedPoolSize(spec);

        // Optimal total = (4 * 2) + 1 = 9
        // 9 / 10 = 0 -> constrained to minimum 2 connections per pod
        assertThat(recommendation.connectionsPerPod()).isEqualTo(2);
        assertThat(recommendation.totalClusterConnections()).isEqualTo(20);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when DB cores are invalid")
    void shouldThrowOnInvalidCores() {
        var invalidSpec = new PoolSizingCalculator.HardwareSpec(0, 1, 1);
        assertThatThrownBy(() -> calculator.calculateRecommendedPoolSize(invalidSpec))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
