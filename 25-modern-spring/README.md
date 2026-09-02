# Module 25: Modern Spring Boot 3.4+ & Java 21 LTS

> **Module Code**: `MOD-25`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Spring 6.2 | RestClient & HttpExchange | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master modern Spring Boot 3.4+ development on Java 21 LTS: Records as immutable DTOs, Sealed Interfaces and exhaustive Pattern Matching for switch with record deconstruction, Spring Boot 3.4 native structured logging formats (`ecs`, `logstash`), modern `@MockitoBean` / `@MockitoSpyBean` Bean Override APIs, declarative HTTP interfaces via `@HttpExchange` and `HttpServiceProxyFactory`, synchronous fluent HTTP client `RestClient` replacing legacy `RestTemplate` on Virtual Threads, and Spring AI architecture (portable `ChatModel`, `PromptTemplate`, `BeanOutputConverter<T>`, and PostgreSQL `pgvector` RAG).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-25-01** | [`01-java-21-lts-in-spring-records-pattern-matching-and-sealed-types.md`](./01-java-21-lts-in-spring-records-pattern-matching-and-sealed-types.md) | Records as DTOs, sealed algebraic types, and exhaustive pattern matching without `default:` branches. |
| **SB-25-02** | [`02-spring-boot-3-4-features-structured-logging-and-mockitobean.md`](./02-spring-boot-3-4-features-structured-logging-and-mockitobean.md) | Native structured JSON logging, Bean Override API (`@MockitoBean`), and Spring Boot 3.4 innovations. |
| **SB-25-03** | [`03-declarative-http-interfaces-httpexchange-vs-openfeign.md`](./03-declarative-http-interfaces-httpexchange-vs-openfeign.md) | `@HttpExchange`, `HttpServiceProxyFactory`, GraalVM AOT native readiness, and comparing with OpenFeign. |
| **SB-25-04** | [`04-restclient-the-modern-fluent-synchronous-http-client.md`](./04-restclient-the-modern-fluent-synchronous-http-client.md) | Spring 6.1 `RestClient` fluent API, error handling, and Virtual Thread pairing vs `WebClient`. |
| **SB-25-05** | [`05-spring-ai-architecture-models-prompts-and-pgvector.md`](./05-spring-ai-architecture-models-prompts-and-pgvector.md) | Spring AI SPIs, `BeanOutputConverter<T>` schema enforcement, and PgVector RAG similarity pipelines. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/modern/`](./src/main/java/com/spring/interview/modern/):

```
25-modern-spring/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/modern/
    │   ├── model/ModernUser.java                                # Sealed interface with Record implementations
    │   ├── client/UserHttpClient.java                           # Declarative @HttpExchange client interface
    │   └── SpringModernApplication.java                         # Executable application entrypoint
    └── test/java/com/spring/interview/modern/                   # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
