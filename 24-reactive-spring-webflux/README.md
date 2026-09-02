# Module 24: Reactive Spring with WebFlux

> **Module Code**: `MOD-24`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Project Reactor 3.7 | Spring WebFlux | StepVerifier | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master reactive, non-blocking architectures in Spring WebFlux: the Reactive Streams specification and backpressure contract (`Publisher`, `Subscriber`, `Subscription.request(n)`), Project Reactor functional operator pipelines (`Mono`, `Flux`, `map`, `flatMap`, `concatMap`, `zip`, `onErrorResume`, `publishOn` vs `subscribeOn`), Reactor Netty event loop internals (1 thread per CPU core) vs Servlet thread-per-request models, non-blocking SQL data access with Spring Data R2DBC and `TransactionalOperator`, testing reactive streams with `StepVerifier`, and the architectural decision matrix: when to choose Spring WebFlux (event-driven gateways, SSE, real-time data feeds) vs Spring MVC with Java 21 Virtual Threads (`Loom`).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-24-01** | [`01-reactive-streams-specification-and-backpressure-contract.md`](./01-reactive-streams-specification-and-backpressure-contract.md) | Reactive Streams push-pull contract, demand signaling (`request(n)`), and buffer overflow policies. |
| **SB-24-02** | [`02-project-reactor-mono-flux-and-operator-chains.md`](./02-project-reactor-mono-flux-and-operator-chains.md) | `Mono` vs `Flux`, `flatMap` parallel merging, `publishOn` vs `subscribeOn`, and avoiding `.block()` traps. |
| **SB-24-03** | [`03-spring-webflux-architecture-netty-event-loops.md`](./03-spring-webflux-architecture-netty-event-loops.md) | Reactor Netty event loops (1 thread per core), OS `epoll` multiplexing, and why blocking kills Netty. |
| **SB-24-04** | [`04-reactive-data-access-r2dbc-and-transactionaloperator.md`](./04-reactive-data-access-r2dbc-and-transactionaloperator.md) | R2DBC non-blocking SQL drivers vs JDBC, `TransactionalOperator`, and Reactor Context transactions. |
| **SB-24-05** | [`05-webflux-vs-virtual-threads-architectural-decision-matrix.md`](./05-webflux-vs-virtual-threads-architectural-decision-matrix.md) | The definitive decision framework: Project Reactor WebFlux vs Spring MVC on Java 21 Virtual Threads. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/webflux/`](./src/main/java/com/spring/interview/webflux/):

```
24-reactive-spring-webflux/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/webflux/
    │   ├── model/Product.java                                   # Immutable Java 21 Product Record
    │   ├── service/ReactiveProductService.java                  # Non-blocking Mono & Flux operator service
    │   ├── controller/ReactiveProductController.java            # WebFlux REST controller
    │   └── SpringWebFluxApplication.java                        # Executable application entrypoint
    └── test/java/com/spring/interview/webflux/                  # 100% Mocked Tier Test Suite (4 StepVerifier Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
