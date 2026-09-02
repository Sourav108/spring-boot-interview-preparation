# 15-05: Session Management & SecurityContextHolder Storage Strategies

> **Module**: `MOD-15: Spring Security`
> **Topic ID**: `SB-15-05`
> **Prerequisites**: `SB-15-01`, `SB-15-02`
> **Primary Technology**: Java 21 LTS | SecurityContext | Concurrency & Virtual Threads
> **Verification Date**: 2026-09-01

---

## 1. Problem
How does Spring Security store authenticated user details across method calls within an HTTP request, and how do you ensure security context is preserved when spawning asynchronous threads or using Java 21 Virtual Threads?

---

## 2. Why It Exists: SecurityContextHolder Strategies
`SecurityContextHolder` stores the `SecurityContext` using pluggable strategies:

| Strategy | Property Value | Storage Mechanism | Concurrency Behavior |
|---|---|---|---|
| **`MODE_THREADLOCAL`** *(Default)* | `MODE_THREADLOCAL` | `ThreadLocal<SecurityContext>` | Bound to current thread; cleared at end of request |
| **`MODE_INHERITABLETHREADLOCAL`** | `MODE_INHERITABLETHREADLOCAL` | `InheritableThreadLocal` | Inherited by child threads created by parent thread |
| **`MODE_GLOBAL`** | `MODE_GLOBAL` | Static field | Shared across all threads (for standalone Swing/desktop apps) |

---

## 3. Asynchronous Security Context Propagation
When using `@Async` or `CompletableFuture`, `MODE_THREADLOCAL` does not propagate context to thread pool workers automatically.
*Solution*: Use `DelegatingSecurityContextAsyncTaskExecutor` or `SecurityContext.wrap()`:

```java
Runnable task = () -> {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // Safely access authenticated principal in async thread!
};
Runnable secureTask = new DelegatingSecurityContextRunnable(task);
executor.execute(secureTask);
```

---

## 4. Session Creation Policies in REST APIs

```java
// Stateless REST API configuration
http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
```

| Policy | Behavior | Use Case |
|---|---|---|
| **`ALWAYS`** | Always creates an `HttpSession` if none exists | Stateful web applications |
| **`NEVER`** | Never creates a session, but will use one if it exists | Hybrid integration |
| **`IF_REQUIRED`** *(Default)* | Creates a session only if required (e.g. login) | Standard MVC web apps |
| **`STATELESS`** | **Never creates or uses `HttpSession`** | **Stateless REST APIs with JWT / Bearer tokens** |

---

## 5. Common Mistakes
- **Assuming `@Async` methods inherit security context by default**: Without context propagation, `SecurityContextHolder.getContext().getAuthentication()` returns `null` in background threads.

---

## 6. Interview Questions
1. **SDE2**: What is the default storage strategy for `SecurityContextHolder`?
2. **Senior**: How does `SecurityContextHolder` interact with Java 21 Virtual Threads and thread pools?

---

## 7. Interview Answer (Senior Level)
"By default, `SecurityContextHolder` uses `MODE_THREADLOCAL`, storing the authenticated security context in a `ThreadLocal` variable tied to the executing request thread. At the end of request processing, `SecurityContextHolderFilter` clears the context via `SecurityContextHolder.clearContext()` to prevent cross-request leakage in pooled servlet threads. With Java 21 Virtual Threads, millions of lightweight virtual threads are cheap to create, but spawning asynchronous background tasks via `CompletableFuture` will lose the security context unless explicitly wrapped in a `DelegatingSecurityContextExecutorService` or `DelegatingSecurityContextRunnable`."
