# Production Version Matrix & Compatibility Guide

> **Status**: Pinned Stable GA Baseline
> **Verification Date**: 2026-09-01
> **Enforcement Policy**: Strict GA only — No Milestone, Snapshot, or Release Candidates.

---

## 📌 Core Framework & Runtime Baseline

| Component | Pinned Version | Scope / BOM Source | Minimum Required |
|---|:---:|---|---|
| **Java Runtime (JDK)** | `21` (LTS) | Baseline compiler `<release>21</release>` | OpenJDK 21+ |
| **Spring Boot** | `3.4.13` (GA) | `org.springframework.boot:spring-boot-dependencies` | 3.4.0+ |
| **Spring Framework** | `6.2.2` (GA) | Managed by Spring Boot `3.4.13` BOM | 6.2.0+ |
| **Spring Security** | `6.4.2` (GA) | Managed by Spring Boot `3.4.13` BOM | 6.4.0+ |
| **Spring Data Release Train** | `2024.1.2` (GA) | Managed by Spring Boot `3.4.13` BOM | 2024.1.0+ |
| **Spring Kafka** | `3.3.2` (GA) | Managed by Spring Boot `3.4.13` BOM | 3.3.0+ |
| **Spring Cloud** | `2024.0.0` (Moxton GA) | `org.springframework.cloud:spring-cloud-dependencies` | 2024.0.0 |
| **Spring AI** | `1.0.0` (GA) | `org.springframework.ai:spring-ai-bom` | 1.0.0 |
| **Apache Maven** | `3.9.9+` | Build system | 3.9.0+ |

---

## 🗄️ Persistence, Drivers & Migration Engines

| Component | Pinned Version | Group / Artifact ID |
|---|:---:|---|
| **HikariCP** | `5.1.0` | `com.zaxxer:HikariCP` |
| **Hibernate Core** | `6.6.5.Final` | `org.hibernate.orm:hibernate-core` |
| **PostgreSQL JDBC Driver** | `42.7.5` | `org.postgresql:postgresql` |
| **Flyway Core** | `10.20.1` | `org.flywaydb:flyway-core` |
| **Flyway PostgreSQL Database**| `10.20.1` | `org.flywaydb:flyway-database-postgresql` |
| **Liquibase Core** | `4.29.2` | `org.liquibase:liquibase-core` |
| **Spring Data Redis / Lettuce** | Managed by SB 3.4.13 | `org.springframework.boot:spring-boot-starter-data-redis` |

---

## 🧪 Testing, Mocking & Container Infrastructure

| Component | Pinned Version | Group / Artifact ID |
|---|:---:|---|
| **JUnit Jupiter** | `5.11.4` | `org.junit.jupiter:junit-jupiter` |
| **Mockito** | `5.15.2` | `org.mockito:mockito-core` |
| **AssertJ** | `3.27.3` | `org.assertj:assertj-core` |
| **Testcontainers** | `1.20.4` | `org.testcontainers:testcontainers` |
| **Testcontainers PostgreSQL** | `1.20.4` | `org.testcontainers:postgresql` |
| **Testcontainers Kafka** | `1.20.4` | `org.testcontainers:kafka` |
| **WireMock** | `3.10.0` | `org.wiremock:wiremock-standalone` |
| **Awaitility** | `4.2.2` | `org.awaitility:awaitility` |

---

## 📊 Observability, Actuator & Telemetry

| Component | Pinned Version | Group / Artifact ID |
|---|:---:|---|
| **Micrometer Core** | `1.14.3` | `io.micrometer:micrometer-core` |
| **Micrometer Tracing** | `1.4.2` | `io.micrometer:micrometer-tracing-bridge-otel` |
| **OpenTelemetry SDK** | `1.44.1` | `io.opentelemetry:opentelemetry-sdk` |
| **Logback** | `1.5.16` | `ch.qos.logback:logback-classic` |
| **Jackson Databind** | `2.18.2` | `com.fasterxml.jackson.core:jackson-databind` |

---

## 🐳 Docker Base Image Matrix

| Container Service | Recommended Tag / Version | Production Purpose |
|---|---|---|
| **PostgreSQL** | `postgres:16-alpine` | Relational database & Testcontainers integration |
| **Redis** | `redis:7-alpine` | Distributed cache, rate limiter & session store |
| **Apache Kafka** | `apache/kafka:3.9.0` (or `confluentinc/cp-kafka:7.7.1`) | Event-driven messaging & distributed log |
| **Zipkin** | `openzipkin/zipkin:3.4` | Distributed tracing visualizer |
| **Prometheus** | `prom/prometheus:v2.54.1` | Time-series metrics collection |
| **Grafana** | `grafana/grafana:11.2.0` | Production telemetry dashboards |

---

## 🛡️ Compatibility Invariant Rules

1. **Java 21 LTS Compatibility**: All code utilizes Java 21 features (Records, Pattern Matching for `switch`, Sealed Classes, Virtual Threads `Thread.ofVirtual()`, Sequenced Collections).
2. **Spring Security 6.x Invariant**: Zero usage of deprecated `WebSecurityConfigurerAdapter`. All security configurations exclusively use `SecurityFilterChain` beans.
3. **No Milestone / RC Dependencies**: Production modules and runnable examples strictly depend on finalized, released artifacts available from Maven Central.
