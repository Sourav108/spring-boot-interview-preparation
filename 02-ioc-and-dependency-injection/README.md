# Module 02: IoC and Dependency Injection

> **Module Code**: `MOD-02`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Framework 6.2.2 | Spring Boot 3.4.13 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into Inversion of Control and Dependency Injection mechanics: why constructor injection is the enterprise gold standard over field and setter injection, the functional distinction of Spring stereotypes (`@Component`, `@Service`, `@Repository`, `@Controller`), deterministic bean resolution (`@Primary`, `@Qualifier`, Map/List strategy injection), the Three-Level Singleton Cache internals, and resolving circular dependencies via `@Lazy` proxies.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-02-01** | [`01-constructor-vs-setter-vs-field-injection.md`](./01-constructor-vs-setter-vs-field-injection.md) | Constructor vs setter vs field injection, immutability (`final`), and pure Java testability. |
| **SB-02-02** | [`02-stereotypes-component-service-repository-controller.md`](./02-stereotypes-component-service-repository-controller.md) | Stereotype hierarchy, semantic boundaries, and `@Repository` PersistenceExceptionTranslation. |
| **SB-02-03** | [`03-bean-resolution-qualifier-primary-autowired.md`](./03-bean-resolution-qualifier-primary-autowired.md) | The 5-step bean resolution algorithm, `@Primary`, `@Qualifier`, and dynamic Map strategy routing. |
| **SB-02-04** | [`04-circular-dependencies-detection-and-resolution.md`](./04-circular-dependencies-detection-and-resolution.md) | DefaultSingletonBeanRegistry 3-level cache, constructor cycle limits, and `@Lazy` proxy resolution. |
| **SB-02-05** | [`05-dependency-graphs-and-hidden-dependencies.md`](./05-dependency-graphs-and-hidden-dependencies.md) | Eliminating Service Locator anti-patterns and managing dependency graph complexity. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/ioc/`](./src/main/java/com/spring/interview/ioc/):

```
02-ioc-and-dependency-injection/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/ioc/
    │   ├── injection/ConstructorInjectionOrderService.java       # Immutable constructor injection implementation
    │   ├── resolution/PrimaryQualifierResolutionEngine.java      # Disambiguation with @Primary, @Qualifier & Map
    │   └── circular/CircularDependencyResolver.java              # @Lazy proxy resolution for circular dependencies
    └── test/java/com/spring/interview/ioc/                       # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
