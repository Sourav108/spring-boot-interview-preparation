# Module 06: Auto-Configuration

> **Module Code**: `MOD-06`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Auto-Configuration SPI | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master the internal mechanics of Spring Boot auto-configuration: the transition from `spring.factories` to `AutoConfiguration.imports`, conditional configuration annotations (`@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty`, `@ConditionalOnBean`), auto-configuration ordering and precedence (`@AutoConfigureAfter`, `@AutoConfigureBefore`, `@AutoConfigureOrder`), debugging auto-configuration decisions via `ConditionEvaluationReport` (`--debug`), and engineering production-grade custom starters from scratch.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-06-01** | [`01-auto-configuration-discovery-and-imports.md`](./01-auto-configuration-discovery-and-imports.md) | `AutoConfiguration.imports` architecture, AOT compilation, and `AutoConfigurationImportSelector`. |
| **SB-06-02** | [`02-conditional-configuration-annotations.md`](./02-conditional-configuration-annotations.md) | The 5 core `@Conditional` annotations and how `@ConditionalOnMissingBean` drives graceful back-off. |
| **SB-06-03** | [`03-auto-configuration-ordering-and-back-off-mechanics.md`](./03-auto-configuration-ordering-and-back-off-mechanics.md) | Phased execution ordering: user configs vs auto-configs, `@AutoConfigureAfter`, and `@AutoConfigureOrder`. |
| **SB-06-04** | [`04-debugging-auto-configuration-conditions-evaluation-report.md`](./04-debugging-auto-configuration-conditions-evaluation-report.md) | Reading `ConditionEvaluationReport` (`--debug`), analyzing positive/negative matches, and finding why beans were created. |
| **SB-06-05** | [`05-building-custom-spring-boot-starters.md`](./05-building-custom-spring-boot-starters.md) | Engineering enterprise custom starters: naming conventions, properties records, and auto-configuration classes. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/autoconfig/`](./src/main/java/com/spring/interview/autoconfig/):

```
06-auto-configuration/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/autoconfig/
    │   ├── mini/MiniAutoConfigurationEngine.java                # Educational Mini-AutoConfiguration evaluator
    │   └── starter/AuditAutoConfiguration.java                  # Production auto-config with @ConditionalOnMissingBean
    └── test/java/com/spring/interview/autoconfig/               # 100% Mocked Tier Test Suite (4 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
