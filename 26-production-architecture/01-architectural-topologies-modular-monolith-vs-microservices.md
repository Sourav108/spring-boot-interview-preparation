# 26-01: Architecture Topologies: Modular Monolith (Spring Modulith) vs Microservices

> **Module**: `MOD-26: Production Architecture`
> **Topic ID**: `SB-26-01`
> **Prerequisites**: System Design Fundamentals
> **Primary Technology**: Java 21 LTS | Spring Modulith 1.3 | Microservices Decision Matrix
> **Verification Date**: 2026-09-01

---

## 1. Problem
Teams prematurely adopting microservices face operational complexity: distributed transactions, network latency, distributed tracing overhead, Kubernetes cluster costs, and API contract fragility.

---

## 2. Why It Exists: Spring Modulith Architecture
**Spring Modulith** enforces strict logical architectural boundaries within a single deployable JVM artifact:
- Packages represent domain modules (`order`, `inventory`, `payment`).
- Enforces boundary isolation at compile-time/test-time (`ApplicationModules.of(Application.class).verify()`).
- Enables asynchronous in-memory event publication with database-backed Event Publication Registries (zero microservice network overhead!).

---

## 3. Architecture: Comparison Matrix & Trade-offs

```mermaid
flowchart TD
    subgraph Modulith["1. Modular Monolith (Spring Modulith) 🏆 Sweet Spot"]
        M1["Order Module"] -->|ApplicationEvents (Zero Network Hops!)| M2["Inventory Module"]
        M1 -->|Strict Package Isolation| M3["Payment Module"]
        Note1["Single Deployment, Single Database, Zero Distributed Latency ⚡"]
    end

    subgraph Microservices["2. Microservices Network 🌐 High Scale"]
        S1["Order Pod"] -->|Kafka / REST / TCP| S2["Inventory Pod"]
        S1 -->|gRPC / Network Hop| S3["Payment Pod"]
        Note2["Independent Scaling, Polyglot Stacks, High Ops Complexity"]
    end
```

---

## 4. Comprehensive Decision Framework

| Dimension | Monolith (Traditional) | Modular Monolith (Spring Modulith) 🏆 | Microservices |
|---|:---:|:---:|:---:|
| **Code Boundaries** | Tangled (Spaghetti) | **Strictly Enforced via Tests** | Process / Network Boundaries |
| **Data Consistency** | Local ACID Transactions | **Local ACID + Event Registry** | Eventual Consistency / Sagas |
| **Deployment Complexity** | Low | **Low (Single container)** | Very High (Kubernetes, Service Mesh) |
| **Team Size Fit** | 1–5 engineers | **5–50 engineers** | 50+ engineers (Decentralized teams) |
| **Hardware Costs** | Minimal | **Minimal** | High (Redundant pod memory) |

---

## 5. Common Mistakes
- **Splitting into microservices before establishing domain boundaries**: Microservices don't fix bad domain modeling; they turn bad domain design into a distributed nightmare.

---

## 6. Interview Questions
1. **SDE2**: What is Spring Modulith and how does it prevent code spaghetti in monoliths?
2. **Senior**: When is a Modular Monolith superior to a Microservices architecture for an enterprise system?

---

## 7. Interview Answer (Senior Level)
"Spring Modulith provides architectural verification tools and an outbox-style event publication registry that enforces domain encapsulation within a single Spring Boot application: tests use `ApplicationModules.verify()` to fail builds if module A directly accesses internal classes of module B without going through its public API. A Modular Monolith is superior when team size is under 50 engineers and workloads don't require drastically asymmetric hardware scaling: it eliminates distributed network latency, cross-service debugging complexity, serialized RPC costs, and multi-service deployment orchestrations while providing 100% atomic ACID transactional integrity and clear paths to extract microservices later if scaling demands dictate."
