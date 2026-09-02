# 06-05: Engineering Custom Spring Boot Starters from Scratch

> **Module**: `MOD-06: Auto-Configuration`
> **Topic ID**: `SB-06-05`
> **Prerequisites**: `SB-06-01`, `SB-06-02`, `SB-06-03`
> **Primary Technology**: Java 21 LTS | Starter Architecture | SDK Engineering
> **Verification Date**: 2026-09-01

---

## 1. Problem
Enterprise organizations need shared internal SDKs (e.g. centralized multi-tenant security, standardized Kafka telemetry, internal audit logging) that internal microservice teams can add via a single Maven dependency with zero boilerplate.

---

## 2. Why It Exists
Custom Spring Boot Starters encapsulate three core components:
1. **The Core Library**: Domain business logic and clients.
2. **`@ConfigurationProperties`**: Type-safe external configuration binding.
3. **`@AutoConfiguration` Class**: Smart conditional bean factory registered in `AutoConfiguration.imports`.

---

## 3. Architecture: Custom Starter Naming & Structure Standard

```
Official Spring Boot Starters:  spring-boot-starter-{name} (e.g. spring-boot-starter-web)
Third-Party / Custom Starters:  {name}-spring-boot-starter (e.g. acme-audit-spring-boot-starter)
```

```
acme-audit-spring-boot-starter/
├── pom.xml
└── src/
    └── main/
        ├── java/com/acme/audit/
        │   ├── AuditClient.java                   # Core logic
        │   ├── AuditProperties.java               # @ConfigurationProperties("acme.audit")
        │   └── AuditAutoConfiguration.java         # @AutoConfiguration with @ConditionalOnMissingBean
        └── resources/META-INF/spring/
            └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

---

## 4. Production Example in Java 21: Complete Custom Starter

### 1. The Configuration Properties
```java
package com.spring.interview.autoconfig.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "enterprise.telemetry")
public record TelemetryProperties(
    boolean enabled,
    String serviceName,
    String collectorUrl
) {
    public TelemetryProperties {
        if (serviceName == null) serviceName = "unnamed-service";
        if (collectorUrl == null) collectorUrl = "http://localhost:4317";
    }
}
```

### 2. The Auto-Configuration Class
```java
package com.spring.interview.autoconfig.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(TelemetryClient.class)
@EnableConfigurationProperties(TelemetryProperties.class)
@ConditionalOnProperty(prefix = "enterprise.telemetry", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TelemetryAutoConfiguration {

    public interface TelemetryClient {
        String report(String metricName, double value);
    }

    public static class DefaultTelemetryClient implements TelemetryClient {
        private final TelemetryProperties properties;

        public DefaultTelemetryClient(TelemetryProperties properties) {
            this.properties = properties;
        }

        @Override
        public String report(String metricName, double value) {
            return "[" + properties.serviceName() + "@" + properties.collectorUrl() + "] " + metricName + "=" + value;
        }
    }

    @Bean
    @ConditionalOnMissingBean(TelemetryClient.class)
    public TelemetryClient telemetryClient(TelemetryProperties properties) {
        return new DefaultTelemetryClient(properties);
    }
}
```

---

## 5. Common Mistakes
- **Naming custom starters `spring-boot-starter-acme`**: Violates the Spring ecosystem convention reserved strictly for official Spring Boot starters.
- **Forgetting `@ConditionalOnMissingBean` on `@Bean` definitions**: Prevents consuming services from overriding default clients with custom implementations.

---

## 6. Interview Questions
1. **SDE2**: What are the essential files needed to build a custom Spring Boot Starter?
2. **Senior**: How do you design a custom starter to be backwards-compatible across 50+ microservices?

---

## 7. Interview Answer (Senior Level)
"Building a production-grade custom starter requires: 1) A `@ConfigurationProperties` record for type-safe YAML configuration, 2) An `@AutoConfiguration` class registering defaults guarded by `@ConditionalOnClass`, `@ConditionalOnProperty`, and `@ConditionalOnMissingBean`, and 3) A descriptor in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`. To guarantee backward compatibility across 50+ microservices, always provide `matchIfMissing = true` on feature toggles, provide sensible fallback defaults, and allow consuming teams to override beans via `@Bean` without starter crashes."
