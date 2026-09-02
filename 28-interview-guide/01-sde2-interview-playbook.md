# 28-01: SDE2 Spring Boot Interview Playbook

> **Target Role**: SDE2 / Mid-Level Backend Engineer
> **Key Focus Areas**: Core IoC, Bean Lifecycle, Spring MVC, Data JPA & Transactions, Sliced Testing
> **Verification Date**: 2026-09-01

---

## 🎯 What Interviewers Look For at SDE2 Level
1. **First Principles**: Explaining *how* IoC, Dependency Injection, and AOP proxies work internally rather than just quoting annotations.
2. **Spring MVC Mechanics**: The complete lifecycle of an HTTP request through `DispatcherServlet`, `HandlerMapping`, `HandlerAdapter`, and RFC 7807 `ProblemDetail` exception handlers.
3. **Database Transactions**: Correct `@Transactional` usage, self-invocation bypass awareness, and N+1 query diagnosis via `JOIN FETCH`.
4. **Testing Hygiene**: Writing fast test suites using `@WebMvcTest` with `@MockitoBean` instead of defaulting blindly to heavy `@SpringBootTest`.

---

## 📋 The 10 Most Common SDE2 Interview Questions & Answers

### 1. How does Spring resolve circular dependencies between singleton beans?
**Answer**: "Spring resolves circular dependencies in singleton beans using its 3-level cache in `DefaultSingletonBeanRegistry`: `singletonObjects` (level 1: fully initialized beans), `earlySingletonObjects` (level 2: instantiated raw/proxy instances), and `singletonFactories` (level 3: `ObjectFactory` lambdas). When Bean A instantiates, it puts a factory in Level 3 before populating properties. When it requires Bean B, Bean B instantiates and requests Bean A, retrieving the early reference from Level 3/2 and completing its initialization. Bean A then finishes with Bean B injected cleanly. Note that constructor injection circular dependencies cannot be resolved and require redesign or `@Lazy`."

### 2. Why does calling a `@Transactional` method from within the same class fail to start a transaction?
**Answer**: "Spring declarative transactions rely on CGLIB or JDK Dynamic AOP proxies. When an external caller invokes the bean, the call hits the proxy interceptor (`TransactionInterceptor`), which manages the transaction boundary. When calling a method internally (`this.methodB()`), the call executes directly on the raw target instance, completely bypassing the Spring proxy and its transaction advice. Fixes include injecting a self-reference, refactoring the method into a separate service, or using `TransactionTemplate`."

### 3. How do you detect and solve the N+1 Query Problem in Spring Data JPA?
**Answer**: "The N+1 problem occurs when fetching $N$ parent entities with `@OneToMany` lazy relations causes Hibernate to execute 1 initial query for the parents, followed by $N$ separate queries when child collections are accessed in a loop. We detect it via SQL query logging (`show-sql: true` / QuickPerf) and resolve it using `JOIN FETCH` in JPQL, `@EntityGraph(attributePaths = {\"children\"})`, or constructor Record DTO projections."

### 4. What is the difference between `@Component`, `@Service`, and `@Repository`?
**Answer**: "`@Component` is the root generic stereotype for any Spring-managed bean. `@Service` and `@Repository` are meta-annotated with `@Component` for domain clarity. Additionally, `@Repository` triggers automatic exception translation: Spring's `PersistenceExceptionTranslationPostProcessor` catches low-level database vendor SQLExceptions and translates them into Spring's unified unchecked `DataAccessException` hierarchy."

### 5. What are the 4 attributes of `@Cacheable`?
**Answer**: "`value` (the cache name), `key` (SpEL key expression, e.g. `#id`), `condition` (evaluated before method execution to veto caching if false), and `unless` (evaluated after method execution with access to `#result` to veto caching null or error responses)."
