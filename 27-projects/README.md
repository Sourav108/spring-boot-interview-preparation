# Module 27: Runnable Production Projects

> **Module Code**: `MOD-27`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Spring Cloud | Production Architectures
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Explore 12 full-stack, enterprise-grade reference project architectures spanning high-concurrency monoliths, distributed microservice sagas, real-time WebSockets, multi-tenant SaaS, telemetry streaming, and modular monoliths.

---

## 🚀 The 12 Production Capstone Projects

| Code | Project Name | Key Technologies & Patterns | Architectural Highlights |
|:---:|---|---|---|
| **PRJ-01** | [`01-ecommerce-monolith`](./01-ecommerce-monolith/) | Spring Data JPA, PostgreSQL, Redis, HikariCP | Cache-Aside catalog, pessimistic locking on stock, pool sizing formula. |
| **PRJ-02** | [`02-banking-transaction-service`](./02-banking-transaction-service/) | PostgreSQL SERIALIZABLE, Optimistic Locking, Double-Entry | Monotonic lock ordering deadlock prevention, `@Version` retries. |
| **PRJ-03** | [`03-distributed-task-scheduler`](./03-distributed-task-scheduler/) | ShedLock, Redis Distributed Mutex, Spring Scheduling | Distributed leader election, atomic Lua release, zero duplicate cron jobs. |
| **PRJ-04** | [`04-realtime-chat-websocket`](./04-realtime-chat-websocket/) | Spring WebSocket STOMP, Redis Pub/Sub, Netty | Multi-node WebSocket broadcast, Redis cross-pod channel routing. |
| **PRJ-05** | [`05-oauth2-auth-server`](./05-oauth2-auth-server/) | Spring Authorization Server 1.4, PKCE, JWKS | OIDC 1.0, PKCE code challenge verification, RSA cryptographic key rotation. |
| **PRJ-06** | [`06-url-shortener-high-concurrency`](./06-url-shortener-high-concurrency/) | Base62, Redis Caching, Bloom Filter | 100k req/s, Bloom filter penetration shield, HTTP 301 vs 302 analytics. |
| **PRJ-07** | [`07-order-saga-orchestrator`](./07-order-saga-orchestrator/) | Apache Kafka 3.9, State Machine, Outbox | Multi-service Saga orchestrator with automated compensating rollbacks. |
| **PRJ-08** | [`08-iot-telemetry-ingestion`](./08-iot-telemetry-ingestion/) | Spring Kafka Batching, TimescaleDB, LZ4 | 1M events/min ingestion, batch JDBC updates, hypertable time-series. |
| **PRJ-09** | [`09-multi-tenant-saas`](./09-multi-tenant-saas/) | Schema-per-Tenant, AbstractRoutingDataSource, Flyway | JWT tenant claim extraction, dynamic schema resolution, zero cross-tenant leak. |
| **PRJ-10** | [`10-content-feed-redis`](./10-content-feed-redis/) | Redis Sorted Sets (ZSET), Fan-Out on Write | Sub-2ms feed pagination, Hybrid fan-out solving the Celebrity Problem. |
| **PRJ-11** | [`11-api-gateway-rate-limiter`](./11-api-gateway-rate-limiter/) | Spring Cloud Gateway, Redis Lua Token Bucket, JWT | Non-blocking edge gateway, atomic Lua script rate limiter, tiered plans. |
| **PRJ-12** | [`12-spring-modulith-event-driven`](./12-spring-modulith-event-driven/) | Spring Modulith 1.3, Event Publication Registry | Compile-time module verification, transactional outbox domain events. |
