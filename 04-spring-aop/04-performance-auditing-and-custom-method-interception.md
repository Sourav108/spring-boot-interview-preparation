# 04-04: Performance Auditing & Custom Method Interception with @Around

> **Module**: `MOD-04: Spring AOP`
> **Topic ID**: `SB-04-04`
> **Prerequisites**: `SB-04-01`
> **Primary Technology**: Java 21 LTS | Custom Annotations | Around Advice Mechanics
> **Verification Date**: 2026-09-01

---

## 1. Problem
How do you track latency, record SLA breaches, or log slow method executions across arbitrary service methods with zero code duplication?

---

## 2. Why It Exists
Custom annotations paired with `@Around` advice provide a clean, declarative mechanism for method interception and telemetry.

---

## 3. Production Example in Java 21

### 1. Custom Annotation
```java
package com.spring.interview.aop.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackExecutionTime {
    String value() default "";
    long thresholdMs() default 100; // Log warning if duration exceeds threshold
}
```

### 2. The Production Aspect
```java
package com.spring.interview.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class PerformanceAuditingAspect {

    private final ConcurrentHashMap<String, AtomicLong> executionCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalDurationNanos = new ConcurrentHashMap<>();

    @Around("@annotation(trackAnnotation)")
    public Object profileExecutionTime(ProceedingJoinPoint pjp, TrackExecutionTime trackAnnotation) throws Throwable {
        long startNanos = System.nanoTime();
        String methodName = pjp.getSignature().toShortString();

        try {
            return pjp.proceed(); // Execute target method
        } finally {
            long elapsedNanos = System.nanoTime() - startNanos;
            executionCounts.computeIfAbsent(methodName, k -> new AtomicLong()).incrementAndGet();
            totalDurationNanos.computeIfAbsent(methodName, k -> new AtomicLong()).addAndGet(elapsedNanos);

            long elapsedMs = elapsedNanos / 1_000_000;
            if (elapsedMs > trackAnnotation.thresholdMs()) {
                System.err.println("[SLA_BREACH] Method " + methodName + " took " + elapsedMs + "ms (Threshold: " + trackAnnotation.thresholdMs() + "ms)");
            }
        }
    }

    public long getInvocationCount(String methodName) {
        AtomicLong count = executionCounts.get(methodName);
        return count != null ? count.get() : 0;
    }
}
```

---

## 4. Common Mistakes
- **Swallowing exceptions in `@Around`**: Catching `Throwable` inside `@Around` without rethrowing it prevents callers and upstream `@Transactional` rollback interceptors from detecting failures.

---

## 5. Interview Questions
1. **SDE2**: Why must `@Around` advice rethrow exceptions if a method fails?
2. **Senior**: How do you measure method latency accurately without skewing GC or thread context timings?

---

## 6. Interview Answer (Senior Level)
"When implementing `@Around` advice, you must rethrow any caught `Throwable` from `pjp.proceed()`. If an exception is swallowed, upstream interceptors—such as Spring's `TransactionInterceptor`—will assume the method completed normally and commit the transaction rather than rolling it back. For accurate latency measurements, use `System.nanoTime()` (which is monotonic and unaffected by wall-clock drift) and integrate with Micrometer `Timer.Sample` to record P95/P99 histograms."
