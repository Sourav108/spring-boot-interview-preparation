# 25-02: Spring Boot 3.4 Innovations: Structured Logging & @MockitoBean

> **Module**: `MOD-25: Modern Spring`
> **Topic ID**: `SB-25-02`
> **Prerequisites**: `SB-21-03`, `SB-22-04`
> **Primary Technology**: Java 21 LTS | Spring Boot 3.4.13 | Structured Logging & Bean Overrides
> **Verification Date**: 2026-09-01

---

## 1. Problem
Prior to Spring Boot 3.4, configuring JSON structured logging required adding complex external dependencies (Logstash Logback encoder), while mocking beans in tests relied on legacy `@MockBean` annotations with inconsistent caching semantics.

---

## 2. Why It Exists: Spring Boot 3.4 Enhancements
1. **Native Structured Logging**: Spring Boot 3.4 introduces out-of-the-box structured logging formats (`ecs`, `gelf`, `logstash`) configurable directly in `application.yml` without third-party XML appenders!
2. **Spring Framework Bean Override API**: Standardized `@MockitoBean` and `@MockitoSpyBean` annotations replace deprecated `@MockBean` / `@SpyBean`.
3. **Enhanced Docker Compose Integration**: Automatic container provisioning at development startup (`spring-boot-docker-compose`).

---

## 3. Architecture: Native Structured Logging Configuration

In `application.yml`:
```yaml
logging:
  structured:
    format:
      console: logstash   # Options: logstash, ecs (Elastic Common Schema), gelf
      file: ecs
```
*Effect*: Automatically structures all console and file log outputs as machine-parseable JSON containing timestamp, level, thread name, logger name, MDC correlation fields, and exception stack traces.

---

## 4. Modern `@MockitoBean` in Spring Boot 3.4+
```java
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoSpyBean
    private PricingCalculator pricingCalculator;
}
```

---

## 5. Common Mistakes
- **Importing deprecated `@MockBean` from `org.springframework.boot.test.mock.mockito`**: In Spring Boot 3.4, use the modern `@MockitoBean` from `org.springframework.test.context.bean.override.mockito`.

---

## 6. Interview Questions
1. **SDE2**: How do you enable structured JSON logging in Spring Boot 3.4?
2. **Senior**: What architectural improvements does the Spring 6.2 / Spring Boot 3.4 Bean Override API introduce over legacy `@MockBean`?

---

## 7. Interview Answer (Senior Level)
"In Spring Boot 3.4, structured JSON logging is built directly into the core framework via `logging.structured.format.console=logstash|ecs`, eliminating the need for third-party Logstash encoder XML configurations. Furthermore, the Spring 6.2 Bean Override API standardizes test mocking via `@MockitoBean` and `@MockitoSpyBean` under `org.springframework.test.context.bean.override.mockito`. Unlike legacy `@MockBean` which had fragmented lifecycle hooks, the new Bean Override API provides extensible, first-class SPI infrastructure for injecting mocks, recording spies, or creating custom test doubles while maintaining predictable `ApplicationContext` cache keys."
