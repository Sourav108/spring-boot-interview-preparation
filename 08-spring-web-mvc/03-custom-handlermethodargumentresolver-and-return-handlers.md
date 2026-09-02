# 08-03: Custom HandlerMethodArgumentResolver & Return Value Handlers

> **Module**: `MOD-08: Spring Web MVC`
> **Topic ID**: `SB-08-03`
> **Prerequisites**: `SB-08-02`
> **Primary Technology**: Java 21 LTS | Web Extension SPI | Custom Parameter Binding
> **Verification Date**: 2026-09-01

---

## 1. Problem
How do you inject custom contextual data (such as authenticated user principals, tenant IDs, client IP addresses, or parsed idempotency tokens) directly into controller method parameters (`public UserDto getProfile(@CurrentUser UserPrincipal user)`) without repeating boilerplate header parsing in every controller?

---

## 2. Why It Exists
Spring MVC provides the **`HandlerMethodArgumentResolver` SPI** (`org.springframework.web.method.support.HandlerMethodArgumentResolver`). It consists of two methods:
1. `supportsParameter(MethodParameter parameter)`: Evaluates if this resolver applies to the target method parameter (e.g. checking if annotated with `@CurrentUser`).
2. `resolveArgument(...)`: Extracts data from `NativeWebRequest` and returns the resolved Java object.

---

## 3. Production Example in Java 21: `@CurrentUser` Parameter Resolver

### 1. The Custom Annotation
```java
package com.spring.interview.mvc.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {}
```

### 2. The Custom Resolver
```java
package com.spring.interview.mvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    public record UserPrincipal(String userId, String email, String role) {}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
            && parameter.getParameterType().equals(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        String userId = webRequest.getHeader("X-User-Id");
        String email = webRequest.getHeader("X-User-Email");
        String role = webRequest.getHeader("X-User-Role");

        if (userId == null) {
            userId = "anonymous";
            email = "anonymous@system.local";
            role = "ROLE_ANONYMOUS";
        }

        return new UserPrincipal(userId, email, role);
    }
}
```

### 3. Registering in `WebMvcConfigurer`
```java
package com.spring.interview.mvc.config;

import com.spring.interview.mvc.resolver.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcCustomConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
    }
}
```

---

## 4. Common Mistakes
- **Mutating request bodies in Argument Resolvers**: The request input stream can only be read once unless wrapped in a caching request wrapper (`ContentCachingRequestWrapper`).

---

## 5. Interview Questions
1. **SDE2**: What is the purpose of `HandlerMethodArgumentResolver` in Spring MVC?
2. **Senior**: How does Spring MVC prevent reading the HTTP request body multiple times when `@RequestBody` and custom resolvers execute?

---

## 6. Interview Answer (Senior Level)
"`HandlerMethodArgumentResolver` is Spring MVC's extension SPI for converting raw HTTP request data into strongly-typed Java method parameters. Spring matches parameters using `supportsParameter()` and resolves them via `resolveArgument()`. Because the standard `ServletInputStream` can only be read once from the underlying socket, Spring MVC's `RequestResponseBodyMethodProcessor` reads the stream once using Jackson `HttpMessageConverter`. If multiple resolvers or filters need access to the raw payload, the request must be wrapped upstream in a `ContentCachingRequestWrapper`."
