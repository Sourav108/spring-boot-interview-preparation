# Module 26: Production Architecture & System Design

> **Module Code**: `MOD-26`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Hexagonal Architecture | Multi-Tenancy | Idempotency | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise software architecture and system design patterns in Spring Boot: Modular Monoliths (Spring Modulith) vs Microservices architectural trade-offs, Clean / Hexagonal / Ports & Adapters architecture (isolating pure Java domain invariants from Spring `@Entity` and controller frameworks), Multi-Tenancy topologies (Database-per-Tenant, Schema-per-Tenant with Hibernate multi-tenancy and `AbstractRoutingDataSource`, Discriminator column with PostgreSQL RLS), Event Sourcing & CQRS (Command Query Responsibility Segregation, event upcasting, snapshotting), and distributed idempotency key state machines (`PROCESSING` $\rightarrow$ `COMPLETED`) with Redis lease reservation.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-26-01** | [`01-architectural-topologies-modular-monolith-vs-microservices.md`](./01-architectural-topologies-modular-monolith-vs-microservices.md) | Spring Modulith module verification, zero-network in-memory events, and microservices decision matrix. |
| **SB-26-02** | [`02-hexagonal-clean-and-ports-adapters-architecture.md`](./02-hexagonal-clean-and-ports-adapters-architecture.md) | Inbound/Outbound ports, driving/driven adapters, and isolating domain core from framework dependencies. |
| **SB-26-03** | [`03-multi-tenancy-architectures-database-schema-discriminator.md`](./03-multi-tenancy-architectures-database-schema-discriminator.md) | Database vs Schema vs Discriminator column, `AbstractRoutingDataSource`, and Hibernate multi-tenancy. |
| **SB-26-04** | [`04-event-sourcing-and-cqrs-architecture-in-spring.md`](./04-event-sourcing-and-cqrs-architecture-in-spring.md) | Append-only event store, CQRS read projections, snapshotting optimization, and event upcasting. |
| **SB-26-05** | [`05-distributed-idempotency-keys-and-rate-limiting.md`](./05-distributed-idempotency-keys-and-rate-limiting.md) | Idempotency lifecycle states (`PROCESSING`, `COMPLETED`), concurrent collision handling, and TTL expiration. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/architecture/`](./src/main/java/com/spring/interview/architecture/):

```
26-production-architecture/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/architecture/
    │   ├── idempotency/IdempotencyEngine.java                   # Distributed idempotency key manager
    │   └── SpringArchitectureApplication.java                   # Executable application entrypoint
    └── test/java/com/spring/interview/architecture/             # 100% Mocked Tier Test Suite (2 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
