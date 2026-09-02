package com.spring.interview.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Production-style performance auditing aspect using @Around advice.
 */
@Aspect
@Component
public class PerformanceAuditingAspect {

    private final ConcurrentHashMap<String, AtomicLong> invocationCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalDurationsNanos = new ConcurrentHashMap<>();

    @Around("@annotation(trackAnnotation)")
    public Object profileExecutionTime(ProceedingJoinPoint pjp, TrackExecutionTime trackAnnotation) throws Throwable {
        long startNanos = System.nanoTime();
        String methodName = pjp.getSignature().getName();

        try {
            return pjp.proceed();
        } finally {
            long elapsedNanos = System.nanoTime() - startNanos;
            invocationCounts.computeIfAbsent(methodName, k -> new AtomicLong()).incrementAndGet();
            totalDurationsNanos.computeIfAbsent(methodName, k -> new AtomicLong()).addAndGet(elapsedNanos);
        }
    }

    public long getInvocationCount(String methodName) {
        AtomicLong count = invocationCounts.get(methodName);
        return count != null ? count.get() : 0;
    }

    public long getTotalDurationNanos(String methodName) {
        AtomicLong total = totalDurationsNanos.get(methodName);
        return total != null ? total.get() : 0;
    }
}
