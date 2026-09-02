package com.spring.interview.performance.calc;

import org.springframework.stereotype.Component;

/**
 * Utility implementing the proven PostgreSQL & HikariCP pool sizing formula:
 * Pool Size = (Core Count * 2) + Effective Spindle Count
 */
@Component
public class PoolSizingCalculator {

    public record HardwareSpec(int databaseCpuCores, int diskSpindles, int applicationPodCount) {}

    public record PoolRecommendation(int connectionsPerPod, int totalClusterConnections, String sizingRationale) {}

    public PoolRecommendation calculateRecommendedPoolSize(HardwareSpec spec) {
        if (spec.databaseCpuCores() <= 0) {
            throw new IllegalArgumentException("Database CPU core count must be greater than zero");
        }
        int effectiveSpindles = Math.max(1, spec.diskSpindles());
        int optimalTotalDbConnections = (spec.databaseCpuCores() * 2) + effectiveSpindles;

        int pods = Math.max(1, spec.applicationPodCount());
        int perPodConnections = Math.max(2, optimalTotalDbConnections / pods);
        int totalConnections = perPodConnections * pods;

        String rationale = String.format(
            "Based on %d DB CPU cores and %d spindles, total optimal DB connections = %d. Partitioned across %d pods = %d connections/pod.",
            spec.databaseCpuCores(), effectiveSpindles, optimalTotalDbConnections, pods, perPodConnections
        );

        return new PoolRecommendation(perPodConnections, totalConnections, rationale);
    }
}
