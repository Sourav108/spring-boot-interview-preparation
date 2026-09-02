# Module 08: Spring Web MVC

> **Module Code**: `MOD-08`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Framework 6.2.2 | Spring Boot 3.4.13 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into the Spring Web MVC request processing engine: tracing the 12-step `DispatcherServlet.doDispatch()` execution pipeline from socket to JSON response, `RequestMappingHandlerMapping` indexing and `HandlerAdapter` reflection execution, building custom `HandlerMethodArgumentResolver`s (e.g. `@CurrentUser`), `HttpMessageConverter` and Jackson content negotiation, and understanding the architectural boundary between Servlet Filters (`OncePerRequestFilter`) and Spring `HandlerInterceptor`s.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-08-01** | [`01-dispatcherservlet-request-processing-lifecycle.md`](./01-dispatcherservlet-request-processing-lifecycle.md) | Front Controller pattern, the 12-step `DispatcherServlet.doDispatch()` request execution trace. |
| **SB-08-02** | [`02-handlermapping-and-handleradapter-internals.md`](./02-handlermapping-and-handleradapter-internals.md) | `RequestMappingHandlerMapping` $O(1)$ URL index, `HandlerExecutionChain`, and `HandlerAdapter`. |
| **SB-08-03** | [`03-custom-handlermethodargumentresolver-and-return-handlers.md`](./03-custom-handlermethodargumentresolver-and-return-handlers.md) | Custom `HandlerMethodArgumentResolver` SPI resolving `@CurrentUser UserPrincipal` parameters. |
| **SB-08-04** | [`04-httpmessageconverter-and-jackson-serialization.md`](./04-httpmessageconverter-and-jackson-serialization.md) | Content Negotiation (`Accept`), `MappingJackson2HttpMessageConverter`, and preventing infinite recursion. |
| **SB-08-05** | [`05-filters-vs-interceptors-architectural-boundaries.md`](./05-filters-vs-interceptors-architectural-boundaries.md) | Architectural boundaries: Servlet `Filter` (pre-dispatcher, stream wrapping) vs `HandlerInterceptor` (post-mapping). |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/mvc/`](./src/main/java/com/spring/interview/mvc/):

```
08-spring-web-mvc/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/mvc/
    │   ├── config/WebMvcCustomConfiguration.java                # WebMvcConfigurer registering resolvers & interceptors
    │   ├── controller/SampleProfileController.java              # REST Controller utilizing @CurrentUser
    │   ├── filter/AuditLoggingFilter.java                       # OncePerRequestFilter attaching X-Response-Time-Millis
    │   ├── interceptor/RequestCorrelationInterceptor.java       # HandlerInterceptor injecting X-Correlation-Id
    │   ├── resolver/CurrentUser.java                            # Custom parameter annotation
    │   └── resolver/CurrentUserArgumentResolver.java            # ArgumentResolver extracting UserPrincipal from headers
    └── test/java/com/spring/interview/mvc/                      # 100% Mocked Tier Test Suite (5 MockMvc & Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
