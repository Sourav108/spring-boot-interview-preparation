# Spring Boot Learning & Module Dependency Graph

> **Curriculum**: `spring-boot-interview-preparation`
> **Total Modules**: 30
> **Target Mastery**: SDE2, Senior Software Engineer, Senior Backend Engineer, Staff AI/Backend Architect

---

## 🗺️ Master Visual Dependency Graph

```mermaid
flowchart TD
    %% Base Tier: Java & Spring Core
    M01["01. Spring Foundations"] --> M02["02. IoC & Dependency Injection"]
    M02 --> M03["03. Bean Lifecycle & Configuration"]
    M03 --> M04["04. Spring AOP & Proxies"]

    %% Boot & Configuration
    M04 --> M05["05. Spring Boot Fundamentals"]
    M05 --> M06["06. Auto-Configuration Internals"]
    M06 --> M07["07. Configuration & Properties"]

    %% Web Tier
    M07 --> M08["08. Spring Web MVC & DispatcherServlet"]
    M08 --> M09["09. REST API Development"]
    M09 --> M10["10. Validation & Error Handling"]

    %% Data Tier
    M10 --> M11["11. Spring JDBC & Connection Pooling (HikariCP)"]
    M11 --> M12["12. Spring Data JPA & Hibernate Internals"]
    M12 --> M13["13. Transactions & Concurrency Management"]
    M13 --> M14["14. Database Migrations (Flyway / Liquibase)"]

    %% Security Tier
    M10 --> M15["15. Spring Security Architecture"]
    M15 --> M16["16. OAuth2 & JWT Resource Servers"]

    %% Messaging & Distributed Caching
    M14 --> M17["17. Spring Cache & Redis"]
    M14 --> M18["18. Spring Kafka & Event-Driven Messaging"]

    %% Microservices & Resilience
    M18 --> M19["19. Spring Cloud & Microservices"]
    M19 --> M20["20. Resilience & Fault Tolerance"]

    %% Testing & Production Operations (Parallel Cross-Cutting Tracks)
    M14 & M16 & M18 --> M21["21. Testing Spring Applications (Testcontainers)"]
    M20 & M21 --> M22["22. Observability & Production Monitoring (Actuator/Otel)"]
    M22 --> M23["23. Performance Tuning & Diagnostic Profiling"]

    %% Advanced & Architectural Tracks
    M23 --> M24["24. Reactive Spring (WebFlux)"]
    M24 --> M25["25. Modern Spring (Virtual Threads, AOT, GraalVM)"]
    M25 --> M26["26. Production Reference Architectures"]

    %% Capstone Projects & Master Interview Preparation
    M26 --> M27["27. 12 Production Projects"]
    M27 --> M28["28. 300+ Interview Questions Bank"]
    M28 --> M29["29. 40+ Production Debugging Scenarios"]
    M29 --> M30["30. Master Architectural Cheatsheets"]
```

---

## 🧩 Learning Track Breakdown

### 1. Core Runtime Track (Modules 01–07)
Foundations of the Spring IoC container, reflection, `BeanDefinition`, custom lifecycle post-processors, CGLIB/JDK dynamic proxy mechanics, Spring Boot startup sequencing, and condition evaluation.

### 2. Web & API Track (Modules 08–10)
HTTP request dispatching via `DispatcherServlet`, handler mapping/adapter invocation, argument resolution, Jackson message conversion, Bean Validation, and RFC 7807 Problem Details error handling.

### 3. Data & Persistence Track (Modules 11–14)
HikariCP connection pool acquisition, raw JDBC vs `JdbcTemplate`, Hibernate first-level cache, entity state transitions (Transient/Managed/Detached/Removed), N+1 query mitigations, transaction proxy boundaries, isolation anomaly prevention, and zero-downtime database migrations.

### 4. Security & Identity Track (Modules 15–16)
`SecurityFilterChain` ordering, stateless JWT token authentication, resource server token decoding, role-based method authorization (`@PreAuthorize`), and OAuth2 client flows.

### 5. Distributed Systems & Caching Track (Modules 17–20)
Redis cache-aside patterns, cache stampede prevention, distributed locking, Kafka consumer concurrency, idempotent consumer patterns, dead-letter queues, transaction outbox patterns, and Resilience4j circuit breakers.

### 6. Production Engineering & Testing Track (Modules 21–23)
Comprehensive testing using `@WebMvcTest`, `@DataJpaTest`, and Testcontainers with real PostgreSQL/Kafka instances, Actuator health/metrics, OpenTelemetry distributed tracing, and systematic latency bottleneck diagnosis.

### 7. Modern Spring & Reactive Track (Modules 24–26)
Project Reactor (`Mono`/`Flux`), WebFlux event loops vs Virtual Threads (`Thread.ofVirtual()`), Spring Boot AOT compilation, and modular monolith vs microservice reference designs.

### 8. Projects & Career Mastery Track (Modules 27–30)
12 runnable production reference services, 300+ categorized technical interview questions with deep architectural answers, 40+ production SEV-1 debugging exercises, and comprehensive quick-reference cheatsheets.
