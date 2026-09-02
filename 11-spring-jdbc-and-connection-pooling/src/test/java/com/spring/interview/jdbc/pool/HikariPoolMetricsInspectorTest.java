package com.spring.interview.jdbc.pool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HikariPoolMetricsInspectorTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Should inspect HikariPool metrics and verify connection pool bounds")
    void shouldInspectHikariMetrics() {
        var metricsOpt = HikariPoolMetricsInspector.inspectMetrics(dataSource);

        assertThat(metricsOpt).isPresent();
        var metrics = metricsOpt.get();

        assertThat(metrics.totalConnections()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.activeConnections()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.threadsAwaitingConnection()).isZero();
    }
}
