# Module 15: Spring Security 6

> **Module Code**: `MOD-15`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Security 6.4.2 | Method Security & SpEL | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise security architectures in Spring Security 6: `DelegatingFilterProxy` and `FilterChainProxy` routing, the 15+ filter chain pipeline order, modern component-based `SecurityFilterChain` bean definitions using Lambda DSL, the complete authentication workflow (`AuthenticationFilter` -> `ProviderManager` -> `DaoAuthenticationProvider` -> `UserDetailsService` -> `BCryptPasswordEncoder`), `@EnableMethodSecurity` and `@PreAuthorize` SpEL authorization, CSRF vs CORS threat models, OWASP security headers (HSTS, CSP, X-Frame-Options), and `SecurityContextHolder` storage strategies (`MODE_THREADLOCAL`) with Virtual Thread context propagation.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-15-01** | [`01-delegatingfilterproxy-and-securityfilterchain-architecture.md`](./01-delegatingfilterproxy-and-securityfilterchain-architecture.md) | `DelegatingFilterProxy`, `FilterChainProxy`, the 15-filter order, and modern `SecurityFilterChain` `@Bean` configuration. |
| **SB-15-02** | [`02-authentication-manager-providers-and-password-encoders.md`](./02-authentication-manager-providers-and-password-encoders.md) | Authentication pipeline trace: `AuthenticationFilter` -> `AuthenticationManager` -> `DaoAuthenticationProvider` -> `BCryptPasswordEncoder`. |
| **SB-15-03** | [`03-authorization-filter-and-method-security-preauthorize.md`](./03-authorization-filter-and-method-security-preauthorize.md) | `AuthorizationFilter` vs `@EnableMethodSecurity` (`@PreAuthorize`, `@PostAuthorize`), SpEL evaluations. |
| **SB-15-04** | [`04-csrf-cors-and-security-response-headers.md`](./04-csrf-cors-and-security-response-headers.md) | CSRF token repository (Cookie vs Session), CORS preflight headers, and OWASP security headers (HSTS, CSP, nosniff). |
| **SB-15-05** | [`05-session-management-and-securitycontextholder-strategies.md`](./05-session-management-and-securitycontextholder-strategies.md) | `SecurityContextHolder` strategies (`MODE_THREADLOCAL`), stateless REST sessions, and Virtual Thread context propagation. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/security/`](./src/main/java/com/spring/interview/security/):

```
15-spring-security/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/security/
    │   ├── config/SecurityConfiguration.java                   # Modern component-based SecurityFilterChain with Lambda DSL
    │   ├── controller/SecuredResourceController.java            # Endpoints with @PreAuthorize("hasRole('ADMIN')")
    │   └── SpringSecurityApplication.java                      # Executable application entrypoint
    └── test/java/com/spring/interview/security/                # 100% Mocked Tier Test Suite (5 MockMvc Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
