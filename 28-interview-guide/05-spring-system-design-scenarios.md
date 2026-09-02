# 28-05: Spring Boot System Design Scenarios (6 Distributed Architectures)

> **Module**: `MOD-28: Interview Guide`
> **Topic ID**: `SB-28-05`
> **Target Role**: Senior / Staff Architect
> **Verification Date**: 2026-09-01

---

## 🏛️ Scenario 1: Design a Real-Time E-Commerce Flash Sale System (100k Req/s)
- **Architecture**: Spring Cloud Gateway -> Redis Lua Token Bucket Rate Limiter -> Inventory Service with Redis Atomic Stock Decrements (`DECRBY`) -> Kafka `OrderCreated` Topic -> Database Batch Writer.
- **Key Bottleneck Solution**: Decouple order acceptance from database writing. The database is never queried during the flash sale surge; all stock validation is evaluated in Redis memory with atomic Lua scripts.

---

## 🏛️ Scenario 2: Design a Global Payment Orchestration Gateway
- **Architecture**: Spring Boot with Hexagonal Architecture -> Distributed Idempotency Key filter with Redis reservation -> State Machine Saga Orchestrator -> OpenFeign / `@HttpExchange` with Resilience4j Circuit Breakers -> Double-entry PostgreSQL Ledger with Optimistic Locking.
- **Key Bottleneck Solution**: Idempotency filters prevent duplicate payments; Sagas handle multi-currency compensating refunds.

---

## 🏛️ Scenario 3: Design a High-Throughput Notification & Feed Service
- **Architecture**: WebFlux & WebSocket STOMP -> Redis Pub/Sub multi-pod channel router -> Redis Sorted Sets (ZSET) for reverse-chronological user timelines -> Hybrid Fan-out (Push for regular users, Pull for celebrities).
- **Key Bottleneck Solution**: Sub-2ms feed pagination via Redis ZSETs; solves the Celebrity Problem by avoiding 50M write fan-outs.

---

## 🏛️ Scenario 4: Design a Multi-Tenant SaaS B2B Analytics Platform
- **Architecture**: Spring Boot -> Schema-per-Tenant isolation via `AbstractRoutingDataSource` -> JWT Tenant Context extraction -> ClickHouse / TimescaleDB time-series analytics -> Prometheus & OpenTelemetry tracing.
- **Key Bottleneck Solution**: Physical schema isolation guarantees 0% cross-tenant data leakage while running on shared application infrastructure.

---

## 🏛️ Scenario 5: Design a Resilient IoT Telemetry Stream Processing Engine
- **Architecture**: Spring Kafka batch consumers (16 partitions) -> TimescaleDB Hypertables -> PostgreSQL binary `COPY` streaming -> Awaitility automated integration test harness.
- **Key Bottleneck Solution**: Micro-batching 500 records per poll reduces 1,000,000 network roundtrips to 2,000 batch writes per minute.

---

## 🏛️ Scenario 6: Design an Enterprise Identity & Access Management (IAM) Provider
- **Architecture**: Spring Authorization Server 1.4 -> PKCE Code Exchange -> Cryptographic RSA Key Rotation -> Redis Distributed Session Store -> OAuth 2.1 Resource Server with custom Keycloak JWT converter.
- **Key Bottleneck Solution**: Stateless RS256 JWT tokens enable downstream microservices to validate authorization independently via public JWKS without hitting the IAM database on every request.
