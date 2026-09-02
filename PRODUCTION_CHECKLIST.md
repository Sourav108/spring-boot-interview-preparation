# Production Readiness & Enterprise Operational Checklist

> **Standard**: Production-Grade Spring Boot 3.4.x / Java 21 LTS Deployment
> **Target Environment**: High-Throughput Kubernetes Microservices / Cloud-Native Platforms

---

## 🏛️ 1. Application Architecture & Lifecycle

- [ ] **Immutable Configuration**: All configuration values mapped via `@ConfigurationProperties` classes validated at startup using `@Validated`.
- [ ] **Zero Hardcoded Secrets**: Zero API keys, passwords, or private keys in repository commits; injected exclusively via Kubernetes Secrets or HashiCorp Vault.
- [ ] **Graceful Shutdown**: `server.shutdown=graceful` enabled with adequate drain timeout (`spring.lifecycle.timeout-per-shutdown-phase=30s`).
- [ ] **Structured JSON Logging**: Standardized Logback JSON encoder outputting `timestamp`, `level`, `thread`, `logger`, `traceId`, `spanId`, and `message`.
- [ ] **Global Error Taxonomy**: Unified `@RestControllerAdvice` emitting RFC 7807 Problem Details with zero leaked stack traces or raw SQL queries.

---

## 🗄️ 2. Database & Persistence Layer (PostgreSQL / HikariCP / JPA)

- [ ] **HikariCP Pool Sizing**: Maximum pool size calculated based on database hardware capacity and thread pool limits; `connection-timeout=30000ms`, `max-lifetime=1800000ms`, `leak-detection-threshold=2000ms`.
- [ ] **Explicit Schema Migration Management**: Flyway or Liquibase manages all DDL changes; Hibernate `ddl-auto` strictly set to `validate` in production.
- [ ] **Query Execution Timeouts**: Explicit default query timeouts set on `@Transactional(timeout = 5)` and JDBC `PreparedStatement` to prevent hanging locks.
- [ ] **Zero N+1 Query Regressions**: Critical read paths verified with Fetch Joins, Entity Graphs, or DTO projections; SQL logging disabled or piped to slow-query analyzers.
- [ ] **Optimistic Locking on Shared State**: Concurrently modified entities protected with `@Version` annotations and explicit retry handlers.
- [ ] **Transaction Boundary Isolation**: Long-running network I/O, external REST calls, and Kafka publishes excluded from active `@Transactional` methods.

---

## ⚡ 3. Distributed Caching (Redis)

- [ ] **Mandatory TTL on All Keys**: Every cached item configured with an explicit Time-To-Live to prevent unbounded Redis memory growth.
- [ ] **Cache Stampede Protection**: Probabilistic early expiration (XFetch) or distributed mutex locking implemented for high-traffic keys.
- [ ] **Serialization Schema Compatibility**: Redis serializer configured with explicit JSON object mappers (e.g. `GenericJackson2JsonRedisSerializer`) avoiding brittle default Java serialization.
- [ ] **Connection & Command Timeouts**: Lettuce client configured with connection timeout (`2000ms`) and command timeout (`1000ms`) with fallback to database.

---

## 📨 4. Event-Driven Messaging (Kafka)

- [ ] **Consumer Idempotency**: Consumers verify unique event IDs against a persistent deduplication store before executing business logic.
- [ ] **Dead Letter Queue (DLQ / DLT)**: Configured `DefaultErrorHandler` with exponential backoff and automatic routing to Dead Letter Topics after max retries.
- [ ] **Transactional Outbox Pattern**: State mutations and outbox event records committed within the same database transaction; background workers publish to Kafka.
- [ ] **Partition Key Consistency**: Events requiring strict ordering published with consistent partition routing keys.

---

## 🛡️ 5. Security & Identity

- [ ] **SecurityFilterChain Modernization**: Zero usage of deprecated `WebSecurityConfigurerAdapter`; all security paths governed by explicit `SecurityFilterChain` beans.
- [ ] **Stateless JWT Verification**: Cryptographic signature, issuer (`iss`), audience (`aud`), and expiration (`exp`) claims verified per request.
- [ ] **Method-Level Authorization**: Sensitive service endpoints protected by `@PreAuthorize("hasRole('ADMIN')")` with `@EnableMethodSecurity`.
- [ ] **CORS & CSRF Hardening**: CSRF disabled only for stateless APIs with Bearer tokens; strict CORS origin allowlists configured.
- [ ] **Input Sanitization & Rate Limiting**: Request payloads validated with Bean Validation (`@Valid`); IP/token rate limiters configured at the API gateway tier.

---

## 📊 6. Observability & Golden Signals (Actuator & OpenTelemetry)

- [ ] **Health Probe Separation**: Actuator configured with dedicated `/actuator/health/liveness` and `/actuator/health/readiness` endpoints.
- [ ] **Actuator Endpoint Exposure**: Sensitive endpoints (e.g. `env`, `heapdump`, `beans`) restricted or secured; only `health`, `info`, and `prometheus` exposed publicly.
- [ ] **Distributed Tracing Context**: W3C `traceparent` context propagated across all HTTP and Kafka boundaries using Micrometer Tracing and OpenTelemetry.
- [ ] **SLI / SLO Monitoring & Alerting**: Key Golden Signals tracked: Latency (P50, P95, P99), Traffic (RPS), Errors (5xx rate), and Saturation (CPU, Memory, HikariCP pool usage).

---

## 🐳 7. Containerization & Kubernetes Deployment

- [ ] **Multi-Stage Docker Build**: Minimal distroless or Alpine JRE base images; non-root user execution (`USER 10001`).
- [ ] **JVM Container Resource Awareness**: Explicit memory limits configured (`-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0`).
- [ ] **Kubernetes Liveness & Readiness Probes**: Probes point to Actuator endpoints with appropriate `initialDelaySeconds` and `periodSeconds`.
- [ ] **Pod Disruption Budgets & Anti-Affinity**: PDBs defined to maintain minimum replica counts during cluster rolling updates.
