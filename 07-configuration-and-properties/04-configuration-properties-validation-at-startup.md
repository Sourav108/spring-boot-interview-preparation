# 07-04: Fail-Fast Startup Validation on @ConfigurationProperties

> **Module**: `MOD-07: Configuration and Properties`
> **Topic ID**: `SB-07-04`
> **Prerequisites**: `SB-07-02`
> **Primary Technology**: Java 21 LTS | Bean Validation (JSR-380) | Fail-Fast Architecture
> **Verification Date**: 2026-09-01

---

## 1. Problem
If an operator deploys a service with a missing database password or a pool size set to `-5`, the application starts up normally and only crashes hours later when a customer tries to query the database.

---

## 2. Why It Exists
Spring Boot integrates **Jakarta Bean Validation (JSR-380 / Hibernate Validator)** directly into `@ConfigurationProperties` using the `@Validated` annotation. If any property constraint is violated, Spring Boot **aborts startup immediately** with a clear diagnostic message.

---

## 3. Production Example in Java 21: Validated Configuration Record
```java
package com.spring.interview.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.database")
public record DatabasePoolProperties(
    @NotBlank(message = "Database JDBC URL must not be blank")
    String jdbcUrl,

    @NotBlank(message = "Database username must not be blank")
    String username,

    @NotBlank(message = "Database password must not be blank")
    String password,

    @Min(value = 1, message = "Minimum pool size must be at least 1")
    @Max(value = 100, message = "Maximum pool size cannot exceed 100")
    int maxPoolSize,

    @NotNull(message = "Connection timeout must be specified")
    Long connectionTimeoutMs
) {}
```

---

## 4. Startup Failure Diagnostic
If `app.database.maxPoolSize: 0` is passed, Spring Boot produces:

```
Binding to target org.springframework.boot.context.properties.bind.BindResult failed:
    Property: app.database.max-pool-size
    Value: 0
    Reason: Minimum pool size must be at least 1
Action:
    Update your application's configuration
```

---

## 5. Common Mistakes
- **Forgetting `@Validated` on the `@ConfigurationProperties` class**: Without `@Validated`, Jakarta validation annotations (`@NotNull`, `@Min`) are silently ignored during property binding.

---

## 6. Interview Questions
1. **SDE2**: How do you validate `@ConfigurationProperties` values at application startup?
2. **Senior**: Why is fail-fast configuration validation essential in Kubernetes microservice environments?

---

## 7. Interview Answer (Senior Level)
"To enforce startup validation, annotate the `@ConfigurationProperties` class with `@Validated` and apply standard Jakarta Bean Validation constraints (`@NotBlank`, `@Min`, `@Max`, `@Pattern`, `@NotNull`). During the `EnvironmentPrepared` binding phase, Spring's `ConfigurationPropertiesBindingPostProcessor` validates all properties against the Validator. If any constraint fails, it throws a `ConfigurationPropertiesBindException`, crashing the container immediately during deployment. In Kubernetes, this causes the startup probe to fail, preventing broken pods from receiving production user traffic."
