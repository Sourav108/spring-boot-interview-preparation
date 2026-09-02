package com.spring.interview.jdbc.pool;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * Inspection utility accessing HikariPoolMXBean runtime metrics (active, idle, threads waiting).
 */
public class HikariPoolMetricsInspector {

    public record PoolMetrics(
        int totalConnections,
        int activeConnections,
        int idleConnections,
        int threadsAwaitingConnection
    ) {}

    public static Optional<PoolMetrics> inspectMetrics(DataSource dataSource) {
        if (dataSource instanceof HikariDataSource hikariDs) {
            HikariPoolMXBean mxBean = hikariDs.getHikariPoolMXBean();
            if (mxBean != null) {
                return Optional.of(new PoolMetrics(
                    mxBean.getTotalConnections(),
                    mxBean.getActiveConnections(),
                    mxBean.getIdleConnections(),
                    mxBean.getThreadsAwaitingConnection()
                ));
            }
        }
        return Optional.empty();
    }
}
