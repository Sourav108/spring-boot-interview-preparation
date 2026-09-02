# Module 09: REST API Development

> **Module Code**: `MOD-09`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | REST Architecture | Spring Web MVC | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master production-grade REST API engineering with Spring Boot: resource modeling, HTTP verb safety and idempotency semantics, status codes, offset vs keyset (cursor) pagination, eliminating entity leakage via immutable Java 21 Record DTOs, API versioning strategies (URI vs Header vs Media Type), and implementing the Idempotency Key pattern for safe distributed mutations.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-09-01** | [`01-rest-resource-modeling-and-http-semantics.md`](./01-rest-resource-modeling-and-http-semantics.md) | REST resource modeling, HTTP verb safety & idempotency, and status code standards. |
| **SB-09-02** | [`02-pagination-sorting-and-filtering-architecture.md`](./02-pagination-sorting-and-filtering-architecture.md) | Offset vs Keyset pagination, `Page<T>` vs `Slice<T>`, and preventing database scan bottlenecks. |
| **SB-09-03** | [`03-dto-pattern-vs-entity-leakage.md`](./03-dto-pattern-vs-entity-leakage.md) | DTO architectural defense: preventing password leaks, infinite recursion, and LazyInitializationExceptions. |
| **SB-09-04** | [`04-api-versioning-strategies-uri-header-query.md`](./04-api-versioning-strategies-uri-header-query.md) | URI path versioning, custom headers, media type negotiation, and sunsetting deprecated APIs. |
| **SB-09-05** | [`05-idempotency-keys-and-safe-mutations.md`](./05-idempotency-keys-and-safe-mutations.md) | Idempotency Key architecture: distributed locks, atomic `SETNX`, and duplicate charge prevention. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/rest/`](./src/main/java/com/spring/interview/rest/):

```
09-rest-api-development/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/rest/
    │   ├── controller/UserRestController.java                   # Production REST CRUD API (POST, GET, PUT, DELETE)
    │   ├── dto/UserRequestDto.java                              # Immutable Java 21 Record with Bean Validation
    │   ├── dto/UserResponseDto.java                             # Clean API output representation
    │   ├── service/InMemoryUserService.java                     # In-memory concurrent user service
    │   └── SpringRestApiApplication.java                        # Executable application entrypoint
    └── test/java/com/spring/interview/rest/                     # 100% Mocked Tier Test Suite (MockMvc CRUD Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
