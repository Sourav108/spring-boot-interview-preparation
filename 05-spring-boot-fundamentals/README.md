# Module 05: Spring Boot Fundamentals

> **Module Code**: `MOD-05`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Spring Framework 6.2.2 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into Spring Boot fundamentals and runtime mechanics: decomposing `@SpringBootApplication` into its three meta-annotations, tracing the 7-phase `SpringApplication` startup sequence, understanding how embedded web servers (Tomcat, Undertow, Jetty) are instantiated and integrated with Java 21 Virtual Threads, understanding starter dependencies and BOM dependency management, and implementing custom `FailureAnalyzer` diagnostic hooks.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-05-01** | [`01-springbootapplication-meta-annotation-decomposition.md`](./01-springbootapplication-meta-annotation-decomposition.md) | `@SpringBootApplication` composition, `@SpringBootConfiguration`, `@EnableAutoConfiguration`, `@ComponentScan`. |
| **SB-05-02** | [`02-springapplication-bootstrap-and-startup-lifecycle.md`](./02-springapplication-bootstrap-and-startup-lifecycle.md) | 7-phase startup sequence, `EnvironmentPrepared`, `ApplicationStarted`, and `ApplicationReadyEvent`. |
| **SB-05-03** | [`03-embedded-servlet-containers-tomcat-undertow-jetty.md`](./03-embedded-servlet-containers-tomcat-undertow-jetty.md) | Embedded Tomcat architecture, `ServletWebServerFactory`, and enabling Java 21 Virtual Threads. |
| **SB-05-04** | [`04-starter-dependencies-and-transitive-dependency-management.md`](./04-starter-dependencies-and-transitive-dependency-management.md) | Starter POMs composition, `spring-boot-dependencies` BOM, and overriding transitive versions. |
| **SB-05-05** | [`05-startup-failure-analysis-and-custom-failure-analyzers.md`](./05-startup-failure-analysis-and-custom-failure-analyzers.md) | `FailureAnalyzer` SPI, actionable remediation reports, and converting startup crashes into diagnostic actions. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/boot/`](./src/main/java/com/spring/interview/boot/):

```
05-spring-boot-fundamentals/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/boot/
    │   ├── lifecycle/StartupLifecycleEventListener.java         # Listener tracking SpringApplication state transitions
    │   └── diagnostics/CustomPortConflictFailureAnalyzer.java   # Custom FailureAnalyzer for BindException
    └── test/java/com/spring/interview/boot/                     # 100% Mocked Tier Test Suite (2 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
