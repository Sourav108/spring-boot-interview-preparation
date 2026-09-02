# Module 20: Resilience & Fault Tolerance

> **Module Code**: `MOD-20`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Resilience4j 2.2.0 | Circuit Breaker & Rate Limiter | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master high-throughput resilience patterns in Spring Boot using Resilience4j: the 5 core fault-tolerance patterns (Circuit Breaker, Rate Limiter, Retry, Bulkhead, TimeLimiter), aspect execution order, CircuitBreaker Ring Bit Buffer sliding window (`COUNT_BASED` vs `TIME_BASED`) state transitions (CLOSED $\rightarrow$ OPEN $\rightarrow$ HALF_OPEN), strict fallback method signature contracts (`Throwable` handling), Token Bucket vs Leaky Bucket vs Sliding Window rate limiting algorithms, and `SemaphoreBulkhead` vs `ThreadPoolBulkhead` concurrency isolation on Java 21 Virtual Threads.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-20-01** | [`01-resilience-patterns-circuit-breaker-rate-limiter-retry-bulkhead.md`](./01-resilience-patterns-circuit-breaker-rate-limiter-retry-bulkhead.md) | The 5 resilience patterns, cascading failure containment, and aspect execution ordering contract. |
| **SB-20-02** | [`02-resilience4j-circuitbreaker-internals-and-sliding-windows.md`](./02-resilience4j-circuitbreaker-internals-and-sliding-windows.md) | Ring Bit Buffer circular bit arrays, zero-GC bit-shift math, and CLOSED/OPEN/HALF_OPEN transitions. |
| **SB-20-03** | [`03-fallback-mechanisms-method-signatures-and-exception-hierarchies.md`](./03-fallback-mechanisms-method-signatures-and-exception-hierarchies.md) | Fallback method signature contracts, exact parameter matching, and specific exception hierarchies. |
| **SB-20-04** | [`04-rate-limiting-token-bucket-leaky-bucket-and-sliding-log.md`](./04-rate-limiting-token-bucket-leaky-bucket-and-sliding-log.md) | Token Bucket vs Leaky Bucket vs Sliding Window counters, boundary burst vulnerabilities. |
| **SB-20-05** | [`05-bulkhead-architecture-threadpool-vs-semaphore.md`](./05-bulkhead-architecture-threadpool-vs-semaphore.md) | `SemaphoreBulkhead` (thread borrowing) vs `ThreadPoolBulkhead` (dedicated worker pool), Java 21 Virtual Threads. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/resilience/`](./src/main/java/com/spring/interview/resilience/):

```
20-resilience-and-fault-tolerance/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/resilience/
    │   ├── service/ExternalPaymentResilienceService.java        # @CircuitBreaker, @RateLimiter, @Retry with fallbacks
    │   └── SpringResilienceApplication.java                     # Executable application entrypoint
    └── test/
        ├── java/com/spring/interview/resilience/                # 100% Mocked Tier Test Suite (2 Unit Tests)
        └── resources/application.yml                            # Resilience4j sliding window & retry configuration
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
