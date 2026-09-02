# 30-02: Spring Boot Key Properties Reference

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-02`
> **Primary Technology**: Spring Boot 3.4.13 `application.yml` Reference
> **Verification Date**: 2026-09-01

---

## ⚙️ Essential Server & Virtual Threads Properties
```yaml
server:
  port: 8080
  shutdown: graceful                              # Drains in-flight requests on SIGTERM
  tomcat:
    threads:
      max: 200                                   # Standard platform thread ceiling
      min-spare: 10
    connection-timeout: 20000ms

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s              # Graceful shutdown wait limit
  threads:
    virtual:
      enabled: true                              # 🏆 Java 21 Loom Virtual Threads for Tomcat & @Async
```

---

## 🗄️ HikariCP Connection Pool Properties
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 15                      # Fixed pool size (Formula: DB cores * 2 + spindles)
      minimum-idle: 15                           # Keep equal to maximum-pool-size
      connection-timeout: 3000ms                 # Max time waiting for pool connection
      idle-timeout: 600000ms                     # 10 minutes
      max-lifetime: 1800000ms                    # 30 minutes
      leak-detection-threshold: 5000ms           # Logs stack trace if connection open > 5s
```

---

## 📊 Observability & Actuator Properties
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics  # Expose only hardened operational endpoints
  endpoint:
    health:
      show-details: when-authorized
      probes:
        enabled: true                            # Kubernetes /health/liveness & /health/readiness
  metrics:
    tags:
      application: ${spring.application.name}

logging:
  structured:
    format:
      console: logstash                          # Native Spring Boot 3.4 JSON structured logs!
```
