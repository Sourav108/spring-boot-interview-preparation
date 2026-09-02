# Master Curriculum: Spring Boot Production & Interview Preparation

> **Target Roles**: SDE2, Senior Backend Engineer, Lead Software Engineer, Staff Systems Architect
> **Total Modules**: 30
> **Baseline**: Java 21 LTS | Spring Boot 3.4.13 | Spring Framework 6.2.2 | Maven 3.9+
> **Source of Truth for Repository Progress**: Initialized Scaffold (`TODO`)

---

## 🧭 Master 30-Module Curriculum Overview

| Module ID | Module Name | Phase | Key Architecture Focus | Status |
|:---:|---|:---:|---|:---:|
| **MOD-01** | [`01-spring-foundations`](./01-spring-foundations/) | Phase 1 | Spring Framework vs Spring Boot, ApplicationContext, BeanFactory, IoC & DI. | `COMPLETE` ✅ |
| **MOD-02** | [`02-ioc-and-dependency-injection`](./02-ioc-and-dependency-injection/) | Phase 1 | Dependency injection mechanisms, constructor injection, circular dependencies, qualifier/primary. | `COMPLETE` ✅ |
| **MOD-03** | [`03-bean-lifecycle-and-configuration`](./03-bean-lifecycle-and-configuration/) | Phase 1 | BeanDefinition, BeanPostProcessor, Aware callbacks, scopes (Singleton/Prototype), lazy init. | `COMPLETE` ✅ |
| **MOD-04** | [`04-spring-aop`](./04-spring-aop/) | Phase 2 | Proxy-based AOP, JDK dynamic vs CGLIB class proxies, join points, pointcuts, self-invocation traps. | `COMPLETE` ✅ |
| **MOD-05** | [`05-spring-boot-fundamentals`](./05-spring-boot-fundamentals/) | Phase 2 | SpringApplication startup lifecycle, embedded web servers (Tomcat), starter dependencies, logging. | `COMPLETE` ✅ |
| **MOD-06** | [`06-auto-configuration`](./06-auto-configuration/) | Phase 2 | Auto-configuration discovery, conditional evaluation (@ConditionalOnMissingBean), custom starters. | `COMPLETE` ✅ |
| **MOD-07** | [`07-configuration-and-properties`](./07-configuration-and-properties/) | Phase 2 | @ConfigurationProperties, YAML/Properties precedence, profile resolution, startup validation. | `COMPLETE` ✅ |
| **MOD-08** | [`08-spring-web-mvc`](./08-spring-web-mvc/) | Phase 3 | DispatcherServlet request flow, HandlerMapping, HandlerAdapter, argument resolvers, message converters. | `TODO` |
| **MOD-09** | [`09-rest-api-development`](./09-rest-api-development/) | Phase 3 | REST API design, idempotency, pagination/sorting, DTO mapping, OpenAPI/Swagger contracts. | `TODO` |
| **MOD-10** | [`10-validation-and-error-handling`](./10-validation-and-error-handling/) | Phase 3 | Bean Validation (@Valid), custom validators, @RestControllerAdvice, RFC 7807 Problem Details. | `TODO` |
| **MOD-11** | [`11-spring-jdbc-and-connection-pooling`](./11-spring-jdbc-and-connection-pooling/) | Phase 4 | JdbcTemplate, DataSource management, HikariCP connection pool sizing, Spring Data JDBC compare. | `TODO` |
| **MOD-12** | [`12-spring-data-jpa-and-hibernate`](./12-spring-data-jpa-and-hibernate/) | Phase 4 | EntityManager, persistence context, dirty checking, entity lifecycle, N+1 query mitigations. | `TODO` |
| **MOD-13** | [`13-transactions-and-concurrency`](./13-transactions-and-concurrency/) | Phase 4 | @Transactional proxy boundaries, propagation, isolation levels, optimistic/pessimistic locking. | `TODO` |
| **MOD-14** | [`14-database-migrations`](./14-database-migrations/) | Phase 4 | Flyway & Liquibase migrations, zero-downtime expand/contract schema evolution, ddl-auto: validate. | `TODO` |
| **MOD-15** | [`15-spring-security`](./15-spring-security/) | Phase 5 | SecurityFilterChain architecture, authentication, authorization, SecurityContext, method security. | `TODO` |
| **MOD-16** | [`16-oauth2-and-jwt`](./16-oauth2-and-jwt/) | Phase 5 | OAuth 2.0 grant types, JWT token parsing and validation, Spring Security Resource Server. | `TODO` |
| **MOD-17** | [`17-spring-cache-and-redis`](./17-spring-cache-and-redis/) | Phase 6 | @Cacheable proxies, RedisTemplate, Lettuce client, cache stampede prevention, distributed locks. | `TODO` |
| **MOD-18** | [`18-spring-kafka-and-messaging`](./18-spring-kafka-and-messaging/) | Phase 6 | KafkaTemplate, @KafkaListener, consumer concurrency, dead letter topics (DLT), outbox pattern. | `TODO` |
| **MOD-19** | [`19-spring-cloud-and-microservices`](./19-spring-cloud-and-microservices/) | Phase 7 | Spring Cloud Gateway, OpenFeign declarative clients, service discovery, distributed config. | `TODO` |
| **MOD-20** | [`20-resilience-and-fault-tolerance`](./20-resilience-and-fault-tolerance/) | Phase 7 | Resilience4j circuit breakers, rate limiters, retries with exponential backoff, bulkheads. | `TODO` |
| **MOD-21** | [`21-testing-spring-applications`](./21-testing-spring-applications/) | Phase 7 | @SpringBootTest, @WebMvcTest, @DataJpaTest, MockMvc, Testcontainers with PostgreSQL & Kafka. | `TODO` |
| **MOD-22** | [`22-observability-and-production`](./22-observability-and-production/) | Phase 7 | Actuator health probes, Micrometer metrics, OpenTelemetry distributed tracing, Golden Signals. | `TODO` |
| **MOD-23** | [`23-performance-and-tuning`](./23-performance-and-tuning/) | Phase 7 | Systematic latency bottleneck diagnosis, connection pool tuning, GC interaction, async workers. | `TODO` |
| **MOD-24** | [`24-reactive-spring-webflux`](./24-reactive-spring-webflux/) | Phase 8 | Project Reactor (Mono/Flux), non-blocking I/O, WebFlux event loops vs Java 21 Virtual Threads. | `TODO` |
| **MOD-25** | [`25-modern-spring`](./25-modern-spring/) | Phase 8 | Virtual Threads in Spring Boot 3.4, Spring AOT, GraalVM native images, modern HTTP interfaces. | `TODO` |
| **MOD-26** | [`26-production-architecture`](./26-production-architecture/) | Phase 8 | Modular Monoliths vs Microservices, Hexagonal/Clean architecture, domain boundary enforcement. | `TODO` |
| **MOD-27** | [`27-projects`](./27-projects/) | Phase 8 | 12 progressively challenging, runnable production capstone projects with full Docker/DB support. | `TODO` |
| **MOD-28** | [`28-interview-questions`](./28-interview-questions/) | Phase 9 | 300+ categorized technical interview questions with deep answers, common traps, and follow-ups. | `TODO` |
| **MOD-29** | [`29-debugging-scenarios`](./29-debugging-scenarios/) | Phase 9 | 40+ production outage post-mortems using the structured SPRING-DEBUG root-cause framework. | `TODO` |
| **MOD-30** | [`30-cheatsheets`](./30-cheatsheets/) | Phase 9 | Comprehensive, fast-lookup architectural cheat sheets across all Spring engineering domains. | `TODO` |

---

## 🎯 The SPRING-DEBUG Interview & Troubleshooting Framework

All debugging exercises and interview scenarios adhere to the **SPRING-DEBUG** methodology:

- **S** — State the observed problem and symptoms clearly.
- **P** — Pinpoint the exact Spring architectural layer involved.
- **R** — Reproduce the failure deterministically with a failing test.
- **I** — Inspect runtime lifecycle, proxy interception, or connection pool state.
- **N** — Narrow down to the definitive root cause.
- **G** — Give the precise code and configuration fix.
- **D** — Discuss design and architectural trade-offs.
- **E** — Explain production operational impact and blast radius.
- **B** — Benchmark, verify, and measure performance recovery.
- **U** — Understand secondary failure modes and edge cases.
- **G** — Guard against regression using automated CI/CD tests.
