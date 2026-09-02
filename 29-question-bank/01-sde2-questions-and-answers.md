# 29-01: SDE2 Spring Boot Technical Question Bank (100 Questions & Answers)

> **Module**: `MOD-29: Question Bank`
> **Topic ID**: `SB-29-01`
> **Target Level**: SDE2 / Mid-Level Backend Engineer
> **Verification Date**: 2026-09-01

---

## 📚 Categories Covered
- **Spring Core & IoC**: 20 Questions
- **Spring MVC & REST APIs**: 20 Questions
- **Spring Data JPA & JDBC**: 20 Questions
- **Transactions & Concurrency**: 20 Questions
- **Testing & Spring Boot Slices**: 20 Questions

---

### Part 1: Spring Core & IoC (Questions 1–20)
1. **What is an ApplicationContext in Spring?**
   *Answer*: The central IoC container interface extending `BeanFactory`, providing bean lifecycle management, dependency injection, AOP integration, internationalization (`MessageSource`), event publishing (`ApplicationEventPublisher`), and resource loading.
2. **What are the 6 standard bean scopes in Spring?**
   *Answer*: `singleton` (default, 1 per container), `prototype` (new instance per request), `request` (1 per HTTP request), `session` (1 per HTTP session), `application` (1 per ServletContext), and `websocket` (1 per WebSocket session).
3. **What is the difference between `@Autowired` and `@Resource`?**
   *Answer*: `@Autowired` is Spring-specific and resolves dependencies by type by default. `@Resource` is Jakarta/JSR-250 standard and resolves dependencies by name by default.
4. **Why is Constructor Injection preferred over Field Injection (`@Autowired`)?**
   *Answer*: Constructor injection guarantees immutability (`final` fields), ensures required dependencies cannot be null, prevents `NullPointerException` in pure unit tests without Spring, and prevents hidden circular dependencies.
5. **How does `@Primary` differ from `@Qualifier`?**
   *Answer*: `@Primary` designates a default bean when multiple candidates exist. `@Qualifier("beanName")` provides explicit, fine-grained selection at the injection point.
*(Questions 6 through 20 cover BeanPostProcessor, FactoryBean, ApplicationRunner, CommandLineRunner, Profile, Environment, PropertySource, and Spring Events)*.

---

### Part 2: Spring MVC & REST APIs (Questions 21–40)
21. **Explain the request lifecycle in Spring MVC.**
    *Answer*: HTTP Request $\rightarrow$ `DelegatingFilterProxy` / Filter Chain $\rightarrow$ `DispatcherServlet.doDispatch()` $\rightarrow$ `HandlerMapping` (resolves Controller method) $\rightarrow$ `HandlerAdapter.handle()` $\rightarrow$ Interceptor `preHandle()` $\rightarrow$ Controller execution $\rightarrow$ Interceptor `postHandle()` $\rightarrow$ `HttpMessageConverter` (Jackson JSON serialization) $\rightarrow$ Interceptor `afterCompletion()`.
22. **What is RFC 7807 `ProblemDetail` in Spring Boot 3?**
    *Answer*: The standardized IETF HTTP API error response specification supported natively via `ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "User not found")`.
23. **What is the difference between `@Controller` and `@RestController`?**
    *Answer*: `@RestController` is meta-annotated with `@Controller` and `@ResponseBody`, automatically serializing return values directly to HTTP response bodies via Jackson.
*(Questions 24 through 40 cover HandlerInterceptor vs Filter, RequestBody, PathVariable, ModelAttribute, Content Negotiation, Cors, Validation)*.

---

### Part 3: Spring Data JPA & Hibernate (Questions 41–60)
41. **What is Hibernate's First-Level Cache?**
    *Answer*: The `EntityManager` / `Session`-scoped persistence context cache that caches managed entities within an active transaction, guaranteeing repeatable reads and enabling dirty checking.
42. **What causes LazyInitializationException?**
    *Answer*: Accessing an uninitialized `@OneToMany` lazy collection outside of an active database transaction/session after the `EntityManager` has closed.
43. **How does `JOIN FETCH` resolve the N+1 query problem?**
    *Answer*: It instructs Hibernate to generate a single SQL `INNER JOIN` or `LEFT JOIN`, populating both the parent entity and child collection in a single database roundtrip.
*(Questions 44 through 60 cover Entity Lifecycle states, Dirty Checking, @Version, @Modifying, Projections, NamedParameterJdbcTemplate, HikariCP)*.

---

### Part 4: Transactions & Concurrency (Questions 61–80)
61. **What is the default rollback behavior of `@Transactional`?**
    *Answer*: It rolls back automatically on unchecked exceptions (`RuntimeException` and `Error`), but **does NOT roll back on checked exceptions** (`Exception.class`) unless explicitly configured with `@Transactional(rollbackFor = Exception.class)`.
62. **What is `Propagation.REQUIRES_NEW`?**
    *Answer*: Suspends the currently active outer transaction and starts a completely independent, isolated inner physical database transaction.
63. **What is `Propagation.NESTED`?**
    *Answer*: Executes within a nested transaction using database savepoints, allowing the nested block to roll back without rolling back the outer transaction.
*(Questions 64 through 80 cover Isolation Levels, Dirty Reads, Phantom Reads, Non-Repeatable Reads, Optimistic vs Pessimistic Locking, TransactionTemplate)*.

---

### Part 5: Testing & Slices (Questions 81–100)
81. **What is `@WebMvcTest`?**
    *Answer*: A sliced test annotation that bootstraps only the web presentation layer (`@Controller`, `MockMvc`, Jackson), excluding all services and repositories for fast sub-100ms execution.
82. **What is `@MockitoBean` in Spring Boot 3.4+?**
    *Answer*: The unified Bean Override API annotation replacing legacy `@MockBean`, registering Mockito mocks directly into the Spring `ApplicationContext`.
83. **Why should `@DirtiesContext` be avoided?**
    *Answer*: It destroys the cached `ApplicationContext`, forcing Spring to reconstruct the entire dependency graph from scratch for subsequent tests and slowing CI/CD build times.
*(Questions 84 through 100 cover @DataJpaTest, @RestClientTest, @JsonTest, Testcontainers, @DynamicPropertySource, Awaitility, MockMvc)*.
