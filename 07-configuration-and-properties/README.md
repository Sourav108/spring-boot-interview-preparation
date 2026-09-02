# Module 07: Configuration and Properties

> **Module Code**: `MOD-07`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Jakarta Validation (JSR-380) | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master external configuration in modern Spring Boot: the 17-level property resolution hierarchy, Relaxed Binding across naming conventions, type-safe `@ConfigurationProperties` vs `@Value`, environment profiles (`@Profile`) and multi-document YAML (`---`), zero-committed secrets management, enforcing fail-fast startup Bean Validation (`@Validated`, `@NotBlank`, `@Min`), and constructor binding with immutable Java 21 Records.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-07-01** | [`01-external-configuration-precedence-hierarchy.md`](./01-external-configuration-precedence-hierarchy.md) | The 17-level property precedence hierarchy, CLI vs Env vs YAML, and Relaxed Binding rules. |
| **SB-07-02** | [`02-configuration-properties-vs-value-annotation.md`](./02-configuration-properties-vs-value-annotation.md) | Structured type-safe `@ConfigurationProperties` vs `@Value`, `Duration` conversion, and immutability. |
| **SB-07-03** | [`03-profiles-environment-management-and-secrets.md`](./03-profiles-environment-management-and-secrets.md) | Multi-document YAML (`on-profile`), Kubernetes ConfigMap mapping, and zero committed secrets. |
| **SB-07-04** | [`04-configuration-properties-validation-at-startup.md`](./04-configuration-properties-validation-at-startup.md) | Fail-fast startup validation: `@Validated`, `@NotBlank`, `@Min`, and `ConfigurationPropertiesBindException`. |
| **SB-07-05** | [`05-immutable-configuration-with-java-records.md`](./05-immutable-configuration-with-java-records.md) | Immutable configuration with Java 21 Records, canonical constructor binding, and `@DefaultValue`. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/config/`](./src/main/java/com/spring/interview/config/):

```
07-configuration-and-properties/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/config/
    │   ├── properties/DatabasePoolProperties.java               # Validated immutable Java 21 Record properties
    │   └── precedence/PropertySourceResolutionEngine.java       # Environment PropertySource inspector
    └── test/java/com/spring/interview/config/                   # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
