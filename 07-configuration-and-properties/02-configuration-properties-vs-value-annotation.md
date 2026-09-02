# 07-02: @ConfigurationProperties vs @Value: Type-Safety & Production Architecture

> **Module**: `MOD-07: Configuration and Properties`
> **Topic ID**: `SB-07-02`
> **Prerequisites**: `SB-07-01`
> **Primary Technology**: Java 21 LTS | Type-Safe Configuration | Immutability
> **Verification Date**: 2026-09-01

---

## 1. Problem
Using `@Value("${app.db.url}")` scattered across dozens of service classes leads to typo-prone string literals, no IDE auto-completion, lack of startup validation, inability to bind complex nested maps or lists, and mutable field values.

---

## 2. Why It Exists
`@ConfigurationProperties` groups related properties into a **structured, strongly-typed, immutable Java 21 Record or Class**. It supports hierarchical structures, Relaxed Binding, JSR-380 Bean Validation, and IDE metadata generation.

---

## 3. Comprehensive Technical Comparison

| Dimension | `@Value("${...}")` | `@ConfigurationProperties(prefix = "...")` |
|---|:---:|:---:|
| **Type-Safety** | Weak (String parsing at injection point) | **Strong (Bound to Java types, Records, Enums, Durations)** |
| **Hierarchical Structure** | No (flat property names only) | **Yes (Nested objects, Lists, Maps)** |
| **Relaxed Binding** | No (exact case match required) | **Yes (kebab-case, camelCase, SNAKE_CASE)** |
| **Startup Bean Validation** | No | **Yes (Full `@Validated` / `@NotNull` / `@Min` support)** |
| **Immutability (Java Records)** | No (requires field/setter injection) | **Yes (Native constructor binding with Java 21 Records)** |
| **IDE Auto-Completion** | No | **Yes (via `spring-boot-configuration-processor`)** |
| **SpEL (Expression Language)** | **Yes (`#{systemProperties['...']}`)** | No |
| **Official Recommendation** | One-off system properties / SpEL only | **100% Preferred for Application Configuration** |

---

## 4. Production Example in Java 21: Record with `@ConfigurationProperties`
```java
package com.spring.interview.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@ConfigurationProperties(prefix = "app.payment")
public record PaymentGatewayProperties(
    String endpointUrl,
    int timeoutSeconds,
    Duration connectionTimeout,
    RetryConfig retry
) {
    public record RetryConfig(int maxAttempts, Duration backoffDelay) {}
}
```

---

## 5. Common Mistakes
- **Scattering `@Value` across 20 classes for the same config prefix**: Violates DRY; if the property name changes, all 20 classes must be updated manually.

---

## 6. Interview Questions
1. **SDE2**: When should you use `@Value` and when should you use `@ConfigurationProperties`?
2. **Senior**: How does `@ConfigurationProperties` handle property binding for `java.time.Duration` and `java.time.DataSize`?

---

## 7. Interview Answer (Senior Level)
"`@ConfigurationProperties` is strongly preferred for structured application configuration because it provides type safety, relaxed binding, hierarchical grouping, startup validation, and immutability via Java 21 Records. `@Value` should only be used for simple one-off values or when SpEL (Spring Expression Language) evaluation is explicitly required. Additionally, Spring Boot provides built-in converters for `@ConfigurationProperties`, allowing string representations such as `10s`, `500ms`, or `10MB` to automatically bind into strongly-typed `java.time.Duration` and `org.springframework.util.unit.DataSize` objects."
