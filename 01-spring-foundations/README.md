# Module 01: Spring Foundations

> **Module Code**: `MOD-01`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Framework 6.2.2 | Spring Boot 3.4.13 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master the architectural foundations of the Spring ecosystem: the evolution from legacy J2EE/EJB to non-invasive POJO enterprise programming, the layered distinction between Jakarta EE, Spring Framework, and Spring Boot, the core design philosophy of Inversion of Control (IoC) and Dependency Injection (DI), lazy `BeanFactory` vs eager `ApplicationContext` initialization, and composition over inheritance.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-01-01** | [`01-what-is-spring-ecosystem-evolution.md`](./01-what-is-spring-ecosystem-evolution.md) | Spring ecosystem hierarchy, POJO-based programming, and enterprise layer decomposition. |
| **SB-01-02** | [`02-spring-framework-vs-spring-boot-vs-jakarta-ee.md`](./02-spring-framework-vs-spring-boot-vs-jakarta-ee.md) | Architectural comparison: Jakarta EE standards vs Spring Framework vs Spring Boot 3.4. |
| **SB-01-03** | [`03-ioc-and-di-design-philosophy.md`](./03-ioc-and-di-design-philosophy.md) | Inversion of Control, Dependency Injection, Hollywood Principle, and loose coupling. |
| **SB-01-04** | [`04-application-context-vs-bean-factory.md`](./04-application-context-vs-bean-factory.md) | Container internals: lazy `BeanFactory` vs eager `ApplicationContext` pre-instantiation. |
| **SB-01-05** | [`05-composition-over-inheritance-in-spring.md`](./05-composition-over-inheritance-in-spring.md) | Avoiding fragile base class anti-patterns via collaborator composition and AOP proxies. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/foundations/`](./src/main/java/com/spring/interview/foundations/):

```
01-spring-foundations/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/foundations/
    │   ├── context/ApplicationContextComparisonDemo.java   # BeanFactory vs ApplicationContext instantiation harness
    │   └── mini/MiniIocContainer.java                       # Educational Mini-IoC container with reflection & DI
    └── test/java/com/spring/interview/foundations/          # 100% Mocked Tier Test Suite (4 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
