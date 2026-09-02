# Module 04: Spring AOP

> **Module Code**: `MOD-04`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Framework 6.2.2 | Spring Boot 3.4.13 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into Spring Aspect-Oriented Programming (AOP): cross-cutting concerns, join points, pointcuts, the 5 advice types (`@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around`), JDK dynamic proxies vs CGLIB bytecode generation, why Spring Boot defaults to `proxyTargetClass=true`, the critical self-invocation proxy bypass trap and its 4 solutions, and how AOP powers declarative transactions (`@Transactional`), caching (`@Cacheable`), and security (`@PreAuthorize`).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-04-01** | [`01-aop-core-concepts-joinpoints-pointcuts-advice.md`](./01-aop-core-concepts-joinpoints-pointcuts-advice.md) | Join points, pointcuts, the 5 advice types, execution pipeline, and Aspect precedence. |
| **SB-04-02** | [`02-jdk-dynamic-proxies-vs-cglib-byte-buddy.md`](./02-jdk-dynamic-proxies-vs-cglib-byte-buddy.md) | Interface reflection proxies vs CGLIB subclass generation; why Spring Boot defaults to CGLIB. |
| **SB-04-03** | [`03-self-invocation-and-proxy-boundaries.md`](./03-self-invocation-and-proxy-boundaries.md) | The self-invocation trap: why `this.method()` bypasses proxies and 4 architectural fixes. |
| **SB-04-04** | [`04-performance-auditing-and-custom-method-interception.md`](./04-performance-auditing-and-custom-method-interception.md) | Custom `@TrackExecutionTime` annotation and `@Around` advice latency telemetry. |
| **SB-04-05** | [`05-connecting-aop-to-transactions-security-and-caching.md`](./05-connecting-aop-to-transactions-security-and-caching.md) | Advisor interceptor pipelines: how Spring chains `@Transactional`, `@PreAuthorize`, and `@Cacheable`. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/aop/`](./src/main/java/com/spring/interview/aop/):

```
04-spring-aop/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/aop/
    │   ├── mini/MiniAopProxy.java                                  # Educational Mini-AOP proxy with advice chains
    │   ├── aspects/TrackExecutionTime.java                         # Custom latency measurement annotation
    │   ├── aspects/PerformanceAuditingAspect.java                  # Production @Around performance auditing aspect
    │   └── selfinvocation/SelfInvocationResolverDemo.java          # Self-invocation bypass & self-injection resolution
    └── test/java/com/spring/interview/aop/                         # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
