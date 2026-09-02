# Module 21: Testing Spring Applications

> **Module Code**: `MOD-21`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Spring Test Slices & @MockitoBean | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master high-velocity, production-grade testing strategies in Spring Boot: the Testing Pyramid distribution (Unit vs Slice vs Integration vs Contract vs E2E), Spring Test Slices (`@WebMvcTest`, `@DataJpaTest`, `@RestClientTest`, `@JsonTest`), Spring Boot 3.4+ bean override architecture (`@MockitoBean` and `@MockitoSpyBean` replacing legacy `@MockBean`), Spring TestContext Framework caching mechanisms and context pollution avoidance, Testcontainers ephemeral Docker databases with `@DynamicPropertySource` injection and the static Singleton Container Pattern, and testing asynchronous / concurrent code with Awaitility polling assertions.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-21-01** | [`01-testing-pyramid-unit-slice-integration-and-contract.md`](./01-testing-pyramid-unit-slice-integration-and-contract.md) | Testing Pyramid 70/20/10 ratio, cost vs fidelity trade-offs, and avoiding slow `@SpringBootTest` build times. |
| **SB-21-02** | [`02-spring-test-slices-webmvctest-datajpatest-and-json.md`](./02-spring-test-slices-webmvctest-datajpatest-and-json.md) | `@WebMvcTest` scoped context, `@DataJpaTest`, `@RestClientTest`, and Spring Boot 3.4 `@MockitoBean`. |
| **SB-21-03** | [`03-mocking-architecture-mockitobean-and-context-pollution.md`](./03-mocking-architecture-mockitobean-and-context-pollution.md) | `ContextCache` composite key mechanics, cache miss penalties, and avoiding `@DirtiesContext` context pollution. |
| **SB-21-04** | [`04-testcontainers-ephemeral-databases-and-dynamicpropertysource.md`](./04-testcontainers-ephemeral-databases-and-dynamicpropertysource.md) | Testcontainers Docker lifecycle, `@DynamicPropertySource` random port injection, and Singleton Container Pattern. |
| **SB-21-05** | [`05-testing-asynchronous-and-concurrent-spring-code.md`](./05-testing-asynchronous-and-concurrent-spring-code.md) | Awaitility polling assertions vs `Thread.sleep()` anti-patterns, and testing `@Async` event pipelines. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/testing/`](./src/main/java/com/spring/interview/testing/):

```
21-testing-spring-applications/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/testing/
    │   ├── model/User.java                                      # Immutable Java 21 User Record
    │   ├── repository/UserRepository.java                       # Repository contract
    │   ├── repository/InMemoryUserRepository.java               # In-memory ConcurrentHashMap repository
    │   ├── service/UserService.java                             # Business service
    │   ├── controller/UserController.java                       # REST presentation controller
    │   └── SpringTestingApplication.java                        # Executable application entrypoint
    └── test/java/com/spring/interview/testing/
        ├── unit/UserServiceTest.java                            # Pure JUnit 5 + Mockito unit tests (3 tests)
        └── slice/UserSliceTest.java                             # @WebMvcTest slice with @MockitoBean (3 tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
