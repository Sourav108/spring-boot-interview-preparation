# 07-03: Profiles, Multi-Document YAML & Secret Management

> **Module**: `MOD-07: Configuration and Properties`
> **Topic ID**: `SB-07-03`
> **Prerequisites**: `SB-07-01`
> **Primary Technology**: Java 21 LTS | Profiles | Enterprise Secret Hygiene
> **Verification Date**: 2026-09-01

---

## 1. Problem
How do you cleanly manage divergent configurations between `local`, `staging`, and `production` environments without duplicating files or accidentally leaking database credentials into git?

---

## 2. Why It Exists
Spring Boot provides **Environment Profiles** (`@Profile` and `spring.profiles.active`). In modern Spring Boot, multi-document YAML allows keeping all profile configurations organized in a single file or split into `application-{profile}.yml` descriptors.

---

## 3. Modern Multi-Document `application.yml`
```yaml
# Default Configuration (Applies to all environments)
spring:
  application:
    name: order-service
  threads:
    virtual:
      enabled: true

app:
  database:
    max-pool-size: 10
    leak-detection-threshold-ms: 2000

---
# Local Development Profile
spring:
  config:
    activate:
      on-profile: local
app:
  database:
    url: jdbc:postgresql://localhost:5432/order_dev
    username: dev_user
    password: dev_password

---
# Production Profile
spring:
  config:
    activate:
      on-profile: prod
app:
  database:
    max-pool-size: 50
    # In production: Values are overridden via Environment Variables!
```

---

## 4. Zero Committed Secrets Policy
In enterprise production:
- **Never commit passwords, private keys, or API tokens to source control**.
- Use Kubernetes Secrets or Cloud Secret Managers (AWS Secrets Manager, HashiCorp Vault).
- Inject them into container environments as environment variables (e.g. `SPRING_DATASOURCE_PASSWORD`).

---

## 5. Interview Questions
1. **SDE2**: How do you activate a specific profile in Spring Boot?
2. **Senior**: How do multi-document YAML documents (`---`) work with `spring.config.activate.on-profile` in Spring Boot 3.4?

---

## 6. Interview Answer (Senior Level)
"Profiles can be activated via `spring.profiles.active=prod` in environment variables or `--spring.profiles.active=prod` in CLI arguments. In Spring Boot 3.4, multi-document YAML files separated by `---` use `spring.config.activate.on-profile: prod` to activate specific document blocks. Properties defined in matching profile blocks override base properties defined in earlier documents. Production secrets must never be stored in profile YAML files; instead, use placeholders or let Kubernetes Secrets inject environment variables that override properties at runtime via Spring's relaxed binding."
