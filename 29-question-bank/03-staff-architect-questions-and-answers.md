# 29-03: Staff / Principal Architect Spring Boot Technical Question Bank (100 Questions & Answers)

> **Module**: `MOD-29: Question Bank`
> **Topic ID**: `SB-29-03`
> **Target Level**: Staff Software Engineer / Principal Architect
> **Verification Date**: 2026-09-01

---

## 📚 Categories Covered
- **Modular Monoliths & Spring Modulith**: 20 Questions
- **Multi-Tenancy & Data Isolation Topologies**: 20 Questions
- **Distributed Sagas, Outbox & Event Sourcing**: 20 Questions
- **JVM Runtime, GC Physics & HikariCP Sizing**: 20 Questions
- **Observability, OpenTelemetry & Production SRE**: 20 Questions

---

### Part 1: Modular Monoliths & Spring Modulith (Questions 201–220)
201. **How does Spring Modulith verify architectural boundaries at build time?**
     *Answer*: By analyzing the application package structure via ArchUnit under the hood with `ApplicationModules.of(Application.class).verify()`, asserting that inter-package dependencies comply strictly with declared module boundaries.
202. **What is the Spring Modulith Event Publication Registry?**
     *Answer*: An outbox pattern implementation that automatically writes domain events published via `ApplicationEventPublisher` into a database table (`event_publication`) in the same transaction as entity changes, marking them completed only after all `@ApplicationModuleListener` invocations succeed.
*(Questions 203 through 220 cover Module naming conventions, NamedInterface, Incomplete Event re-publication, Decoupling domain aggregates)*.

---

### Part 2: Multi-Tenancy & Data Isolation (Questions 221–240)
221. **Explain the trade-offs between Schema-per-Tenant and Discriminator Column multi-tenancy.**
     *Answer*: Schema-per-Tenant provides near physical data isolation and simple tenant offboarding (`DROP SCHEMA`) at moderate operational cost; Discriminator Column is lowest in infrastructure cost but carries severe data leak risks if application queries omit the tenant filter.
222. **How does `AbstractRoutingDataSource` switch databases at runtime?**
     *Answer*: It overrides `determineCurrentLookupKey()`, which queries a context holder (e.g. `TenantContext.getTenant()`) bound to the current thread or async context by an authentication filter.
*(Questions 223 through 240 cover Hibernate MultiTenancyConnectionProvider, Tenant Context Propagation in MDC, Dynamic Flyway Migrations)*.

---

### Part 3: Distributed Sagas & Event Sourcing (Questions 241–260)
241. **Why is Orchestration Saga preferred over Choreography Saga for complex workflows?**
     *Answer*: Orchestration centralizes business state transitions, timeout handling, and compensating rollbacks in a dedicated orchestrator, preventing the "pinball machine" spaghetti event dependencies and cycle detection problems of choreography.
242. **How do you handle Event Schema Evolution (Upcasting) in Event Sourcing?**
     *Answer*: Via Upcaster transformers in the event deserialization pipeline that detect older schema versions and migrate payloads dynamically into current Java record formats before aggregate hydration.
*(Questions 243 through 260 cover Debezium CDC WAL capture, Exactly-Once Processing semantics, Idempotent Consumers, Snapshot optimization)*.

---

### Part 4: JVM Tuning, GC & Connection Physics (Questions 261–280)
261. **Why does Generational ZGC achieve sub-millisecond pauses on multi-terabyte heaps?**
     *Answer*: It executes all garbage collection phases (marking, relocation, reference updating) concurrently with application threads using Colored Pointers (metadata in unused pointer bits) and Load Barriers, restricting Stop-the-World pauses to trivial thread-local handshakes.
262. **What is the mathematical rationale behind the HikariCP sizing formula $T_N = C \times 2 + I$?**
     *Answer*: A single CPU core can physically compute only one thread at a time. Sizing the pool to match active CPU cores plus disk spindle concurrency minimizes kernel context-switching overhead and maximizes L1/L2 cache locality on the database server.
*(Questions 263 through 280 cover AppCDS training archives, GraalVM native image closed-world assumptions, Thread Pinning diagnostics, JFR)*.

---

### Part 5: Observability, OpenTelemetry & SRE (Questions 281–300)
281. **Why must database health checks be excluded from Kubernetes Liveness probes?**
     *Answer*: Liveness probes determine if the container should be killed and restarted. If the database experiences a brief network hiccup, all pods fail their liveness probes simultaneously, causing a cascading cluster-wide restart storm that destroys remaining database capacity.
282. **How does W3C `traceparent` propagate distributed context across HTTP headers?**
     *Answer*: Format `version-trace_id-parent_id-trace_flags` (e.g. `00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01`). Downstream services parse this header to link child spans to the parent distributed trace.
*(Questions 283 through 300 cover Micrometer Tracing bridges, Prometheus histogram percentiles, MDC TaskDecorator, SRE Golden Signals)*.
