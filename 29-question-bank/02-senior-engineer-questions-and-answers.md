# 29-02: Senior Engineer Spring Boot Technical Question Bank (100 Questions & Answers)

> **Module**: `MOD-29: Question Bank`
> **Topic ID**: `SB-29-02`
> **Target Level**: Senior Software Engineer / Senior Backend Engineer
> **Verification Date**: 2026-09-01

---

## 📚 Categories Covered
- **Spring Boot Internals & Auto-Configuration**: 20 Questions
- **Spring AOP & Proxy Architecture**: 20 Questions
- **Spring Security & OAuth 2.1 / JWT**: 20 Questions
- **Distributed Caching & Redis**: 20 Questions
- **Messaging (Kafka) & Resilience (Resilience4j)**: 20 Questions

---

### Part 1: Spring Boot Internals & Auto-Configuration (Questions 101–120)
101. **How does Spring Boot resolve `@ConditionalOnClass` without crashing on missing classes?**
     *Answer*: It uses ASM bytecode parsing (`MetadataReader`) to inspect class metadata in `.class` files without triggering the JVM ClassLoader to load missing classes into memory.
102. **How does Spring Boot determine the order of Auto-Configuration classes?**
     *Answer*: Via `@AutoConfigureOrder`, `@AutoConfigureBefore`, and `@AutoConfigureAfter` declarations in `AutoConfigurationSorter`.
103. **What is `FailureAnalyzer` in Spring Boot?**
     *Answer*: An SPI that intercepts application startup exceptions (e.g. `PortInUseException`, `BeanCreationException`) and formats actionable, human-readable console diagnostics and descriptions.
*(Questions 104 through 120 cover EnvironmentPostProcessor, ApplicationPreparedEvent, SpringApplicationRunListener, Custom Starter development)*.

---

### Part 2: Spring AOP & Proxy Architecture (Questions 121–140)
121. **When does Spring choose CGLIB over JDK Dynamic Proxies?**
     *Answer*: In Spring Boot 2.x and 3.x, CGLIB is the default proxy mechanism (`spring.aop.proxy-target-class=true`). JDK Dynamic Proxies are used only when explicitly configured and the target class implements an interface.
122. **What are the limitations of CGLIB proxies?**
     *Answer*: Cannot proxy `final` classes, cannot override `final` methods, and constructors are called twice (once for raw instance, once for proxy instance).
*(Questions 123 through 140 cover AspectJ pointcut expressions, @Around proceed(), MethodInterceptor, Advisor vs Advice, Order precedence)*.

---

### Part 3: Spring Security & OAuth 2.1 / JWT (Questions 141–160)
141. **How does Spring Security's `SecurityFilterChain` execute?**
     *Answer*: Incoming requests enter the Servlet container and are routed by `DelegatingFilterProxy` to the `FilterChainProxy`, which invokes a sequence of 15+ standard filters (`SecurityContextHolderFilter`, `BearerTokenAuthenticationFilter`, `UsernamePasswordAuthenticationFilter`, `ExceptionTranslationFilter`, `AuthorizationFilter`).
142. **How do you convert Keycloak realm roles in a JWT into Spring `GrantedAuthority`?**
     *Answer*: By registering a custom `Converter<Jwt, AbstractAuthenticationToken>` with `JwtAuthenticationConverter` that extracts nested claims from `realm_access.roles` and prefixes them with `ROLE_`.
*(Questions 143 through 160 cover Stateless SessionCreationPolicy, CSRF defense, CORS preflight handling, Method Security SpEL, Public Key JWKS verification)*.

---

### Part 4: Distributed Caching & Redis (Questions 161–180)
161. **How do you prevent Cache Stampede in Spring Boot?**
     *Answer*: By configuring `@Cacheable(sync = true)` which applies local synchronization, or using probabilistic early expiration (XFetch algorithm) in Redis.
162. **How does Redis implement a Token Bucket rate limiter?**
     *Answer*: Using an atomic Lua script that compares elapsed milliseconds against the token refill rate, increments available tokens, checks capacity, and updates the timestamp atomically.
*(Questions 163 through 180 cover Cache Evict, Cache Put, Redis Serialization JSON vs JDK, Redisson distributed locks, Redis Cluster failover)*.

---

### Part 5: Kafka & Resilience4j (Questions 181–200)
181. **What is the difference between `@RetryableTopic` and standard `DefaultErrorHandler` retries?**
     *Answer*: `DefaultErrorHandler` retries synchronously in-place, blocking the consumer thread on that partition. `@RetryableTopic` publishes failed records to delayed retry topics and commits the main offset, enabling non-blocking stream processing.
182. **What is the aspect execution order in Resilience4j?**
     *Answer*: `Retry (Outer) -> CircuitBreaker -> RateLimiter -> TimeLimiter -> Bulkhead (Inner)`.
*(Questions 183 through 200 cover CircuitBreaker state transitions, Sliding Window Metrics, DLT Handlers, Consumer Rebalance Listeners, Kafka Transactions)*.
