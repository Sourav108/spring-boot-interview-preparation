# Module 22: Observability & Production Readiness

> **Module Code**: `MOD-22`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot Actuator | Micrometer 1.14 | OpenTelemetry Tracing | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise production observability in Spring Boot: Spring Boot Actuator endpoint security hardening, custom `HealthIndicator` for Kubernetes liveness vs readiness probes, Micrometer multidimensional metrics (Counters, Gauges, Timers, Histograms) and Prometheus scraping formats, OpenTelemetry distributed tracing with W3C `traceparent` context propagation across microservices and async threads, SLF4J MDC structured JSON logging with `TaskDecorator` cross-thread preservation, Google SRE's 4 Golden Signals (Latency, Traffic, Errors, Saturation), and zero-downtime rolling update graceful shutdown (`server.shutdown: graceful`).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-22-01** | [`01-spring-boot-actuator-endpoints-and-security-hardening.md`](./01-spring-boot-actuator-endpoints-and-security-hardening.md) | Actuator security exposure, custom `HealthIndicator`, and Kubernetes liveness vs readiness isolation. |
| **SB-22-02** | [`02-micrometer-metrics-counter-gauge-timer-and-prometheus.md`](./02-micrometer-metrics-counter-gauge-timer-and-prometheus.md) | Micrometer meter types, dimensional tagging, and preventing metric cardinality explosions. |
| **SB-22-03** | [`03-distributed-tracing-opentelemetry-w3c-and-micrometer.md`](./03-distributed-tracing-opentelemetry-w3c-and-micrometer.md) | W3C `traceparent` header format, Micrometer Tracing bridge, and OpenTelemetry span propagation. |
| **SB-22-04** | [`04-structured-json-logging-and-mdc-async-propagation.md`](./04-structured-json-logging-and-mdc-async-propagation.md) | SLF4J MDC key-value logging, JSON formatting, and `TaskDecorator` async thread context transfer. |
| **SB-22-05** | [`05-the-4-golden-signals-and-graceful-shutdown.md`](./05-the-4-golden-signals-and-graceful-shutdown.md) | Latency, Traffic, Errors, Saturation alerting, and `server.shutdown=graceful` connection draining. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/observability/`](./src/main/java/com/spring/interview/observability/):

```
22-observability-and-production/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/observability/
    │   ├── health/DatabasePoolHealthIndicator.java              # Custom Actuator HealthIndicator with saturation
    │   ├── metrics/OrderMetricsService.java                     # Micrometer Counter and Timer metrics service
    │   └── SpringObservabilityApplication.java                  # Executable application entrypoint
    └── test/java/com/spring/interview/observability/            # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
