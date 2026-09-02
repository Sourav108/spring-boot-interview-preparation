# 25-03: Declarative HTTP Interfaces: @HttpExchange vs OpenFeign

> **Module**: `MOD-25: Modern Spring`
> **Topic ID**: `SB-25-03`
> **Prerequisites**: `SB-19-02`
> **Primary Technology**: Java 21 LTS | Spring 6 HTTP Interfaces | @HttpExchange
> **Verification Date**: 2026-09-01

---

## 1. Problem
OpenFeign requires pulling in large Spring Cloud dependencies (`spring-cloud-starter-openfeign`), has slower startup reflection overhead, and is tightly coupled to the legacy Netflix ecosystem.

---

## 2. Why It Exists: Spring 6 Declarative HTTP Interfaces
Spring 6 introduces native declarative HTTP interfaces via **`@HttpExchange`** and **`HttpServiceProxyFactory`**, backed by either modern synchronous `RestClient` or non-blocking `WebClient`.

---

## 3. Architecture: Declarative Interface & Client Adaption

```mermaid
flowchart LR
    Interface["@HttpExchange interface UserClient"] --> Factory["HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build()"]
    Factory --> Proxy["Dynamic Client Proxy"]
    Proxy --> RestClient["RestClient (Underlying HTTP transport) ⚡"]
```

---

## 4. Production Example in Java 21
```java
package com.spring.interview.modern.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/users")
public interface UserHttpClient {

    record RemoteUserDto(String id, String username, String email) {}

    @GetExchange("/{id}")
    RemoteUserDto getUserById(@PathVariable("id") String id);

    @PostExchange
    RemoteUserDto createUser(RemoteUserDto userDto);
}
```

### Registering the Proxy Bean
```java
@Configuration
public class HttpClientConfig {

    @Bean
    public UserHttpClient userHttpClient(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("https://api.internal.service").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build();

        return factory.createClient(UserHttpClient.class);
    }
}
```

---

## 5. Comparison: `@HttpExchange` vs OpenFeign

| Dimension | Spring 6 `@HttpExchange` 🏆 | Spring Cloud OpenFeign |
|---|:---:|:---:|
| **Dependency Requirement** | **Core Spring Framework 6+ (Zero extra dependencies)** | Requires Spring Cloud OpenFeign BOM |
| **Underlying HTTP Client** | Pluggable (`RestClient`, `WebClient`, `RestTemplate`) | Custom Feign `Client` (Apache/OkHttp) |
| **GraalVM Native Image** | **Full Native Image AOT Support out-of-the-box ✅** | Requires custom reflection hints |
| **Reactive Support** | **Native `Mono`/`Flux` when backed by WebClient** | Requires Feign Reactive extensions |

---

## 6. Interview Questions
1. **SDE2**: What is `@HttpExchange` in Spring 6?
2. **Senior**: When should you migrate from Spring Cloud OpenFeign to Spring 6 `@HttpExchange` with `RestClient`?

---

## 7. Interview Answer (Senior Level)
"Spring 6 `@HttpExchange` is a built-in declarative HTTP client abstraction that generates client proxies using `HttpServiceProxyFactory`. Unlike OpenFeign which requires heavy Spring Cloud dependencies and custom annotation decoders, `@HttpExchange` is part of core Spring Web, supports GraalVM AOT native compilation out-of-the-box, and can be backed interchangeably by modern synchronous `RestClient` or reactive `WebClient`. Teams should migrate from OpenFeign to `@HttpExchange` for new microservices to reduce dependency bloat, improve boot times, and simplify client maintenance."
