# 02-04: Circular Dependencies: 3-Level Cache Internals & Modern Resolution

> **Module**: `MOD-02: IoC and Dependency Injection`
> **Topic ID**: `SB-02-04`
> **Prerequisites**: `SB-02-01`, `SB-02-03`
> **Primary Technology**: Java 21 LTS | Container Internals | 3-Level Cache Architecture
> **Verification Date**: 2026-09-01

---

## 1. Problem
A **Circular Dependency** occurs when Bean A depends on Bean B, and Bean B depends directly on Bean A (`A -> B -> A`). In constructor injection, neither bean can be constructed first, resulting in `BeanCurrentlyInCreationException`. In Spring Boot 2.6+, circular dependencies are **disabled by default**.

---

## 2. Why It Exists
Historically, Spring supported circular dependencies for setter/field injection using a sophisticated **Three-Level Singleton Cache** in `DefaultSingletonBeanRegistry`:
1. `singletonObjects` (1st level): Fully initialized singleton instances ready for use.
2. `earlySingletonObjects` (2nd level): Early references (instantiated, but before property population / initialization).
3. `singletonFactories` (3rd level): `ObjectFactory` instances capable of exposing early bean references (including early AOP proxies).

---

## 3. Architecture: The 3-Level Cache Flow

```mermaid
sequenceDiagram
    autonumber
    participant App as Spring Container
    participant L3 as 3rd Level: singletonFactories
    participant L2 as 2nd Level: earlySingletonObjects
    participant L1 as 1st Level: singletonObjects

    App->>App: 1. Instantiate Bean A (empty instance)
    App->>L3: 2. Put ObjectFactory for A
    App->>App: 3. Populate A (needs B)
    App->>App: 4. Instantiate Bean B (empty instance)
    App->>App: 5. Populate B (needs A)
    App->>L1: Check L1 for A (MISS)
    App->>L2: Check L2 for A (MISS)
    App->>L3: Check L3 for A (HIT!)
    L3->>L2: Create early reference of A & move to L2
    App->>App: 6. Inject early A into B; Complete B initialization
    App->>L1: Put B into L1
    App->>App: 7. Complete A initialization
    App->>L1: Move A to L1
```

---

## 4. Why Constructor Injection Cannot Use the 3-Level Cache
In constructor injection:
- To instantiate `A`, JVM bytecode requires executing `new A(b)`.
- But `b` does not exist yet.
- To instantiate `B`, JVM requires executing `new B(a)`.
- Neither constructor can return an unpopulated raw reference to place in the 3rd level cache!
- Therefore, **constructor circular dependencies physically cannot be resolved without dynamic proxies (`@Lazy`)**.

---

## 5. Architectural Solutions for Circular Dependencies

| Approach | Mechanism | Recommendation |
|---|---|---|
| **1. Architectural Refactoring**| Extract shared logic into a 3rd service (`C`) | **BEST PRACTICE (100% Clean Architecture)** |
| **2. Event-Driven Decoupling**| Publish `ApplicationEvent` instead of direct injection | **HIGHLY RECOMMENDED** |
| **3. `@Lazy` Injection** | Injects a CGLIB lazy proxy that resolves target on first call | Acceptable tactical workaround |
| **4. Setter Injection** | Allows 3-level cache resolution | Discouraged in Spring Boot 3.4+ |

---

## 6. Production Example: Resolving via `@Lazy` Proxy
```java
package com.spring.interview.ioc.circular;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ServiceA {
    private final ServiceB serviceB;

    // @Lazy tells Spring to inject a CGLIB proxy for ServiceB
    public ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String executeA() {
        return "A called -> " + serviceB.callFromA();
    }

    public String callFromB() {
        return "Hello from ServiceA";
    }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public String executeB() {
        return "B called -> " + serviceA.callFromB();
    }

    public String callFromA() {
        return "Hello from ServiceB";
    }
}
```

---

## 7. Common Mistakes
- **Enabling `spring.main.allow-circular-references=true` in production**: Masking architectural anti-patterns rather than fixing tight coupling.

---

## 8. Interview Questions
1. **SDE2**: Why does Spring Boot disable circular references by default starting in Spring Boot 2.6?
2. **Senior**: Explain how Spring's 3-level cache resolves circular dependencies in setter injection and why it fails in constructor injection.

---

## 9. Interview Answer (Senior Level)
"Spring uses a Three-Level Cache in `DefaultSingletonBeanRegistry` to resolve setter/field circular references: `singletonObjects` (level 1) holds fully initialized beans, `earlySingletonObjects` (level 2) holds raw instantiated beans, and `singletonFactories` (level 3) holds `ObjectFactory` lambdas capable of creating early AOP proxy references. When Bean A is instantiated, its `ObjectFactory` is placed in Level 3. When Bean B asks for A, it pulls A's early reference from Level 3 into Level 2, completing B's creation and unblocking A. This mechanism fails completely in constructor injection because the JVM requires constructor arguments *before* instantiation, so no raw instance exists to register in Level 3. Spring Boot disables circular references by default to prevent tight coupling and hidden runtime deadlocks."
