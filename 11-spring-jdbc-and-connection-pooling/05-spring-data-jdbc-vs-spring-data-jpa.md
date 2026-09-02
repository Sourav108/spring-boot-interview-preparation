# 11-05: Spring Data JDBC vs Spring Data JPA: Architectural Trade-Offs

> **Module**: `MOD-11: Spring JDBC & Connection Pooling`
> **Topic ID**: `SB-11-05`
> **Prerequisites**: `SB-11-01`
> **Primary Technology**: Java 21 LTS | Persistence Architectures | DDD Aggregates
> **Verification Date**: 2026-09-01

---

## 1. Problem
JPA/Hibernate provides immense power (lazy loading, dirty checking, first-level caching) but introduces complex runtime mental overhead: N+1 queries, detached entity bugs, proxy deserialization issues, and unpredictable SQL generation.

---

## 2. Why It Exists
**Spring Data JDBC** is a lightweight, opinionated alternative to Spring Data JPA based on Domain-Driven Design (DDD) **Aggregate Roots**. It has:
- **NO Session / Persistence Context** (no 1st-level cache).
- **NO Dirty Checking** (saving an entity requires an explicit `repository.save()`).
- **NO Lazy Loading Proxies** (aggregates are loaded eagerly in their entirety).
- **NO Caching Magic**: What you write in Java maps directly to predictable, deterministic SQL.

---

## 3. Comprehensive Architectural Comparison Matrix

| Dimension | Spring Data JDBC | Spring Data JPA (Hibernate) |
|---|:---:|:---:|
| **Underlying Engine** | Plain Spring JDBC (`NamedParameterJdbcTemplate`) | Hibernate ORM (`EntityManager`) |
| **Domain Model Philosophy** | DDD Aggregate Roots | Entity Relationship Graphs (JPA) |
| **Lazy Loading** | **NO** (Never generates runtime dynamic proxies) | **YES** (CGLIB / Byte Buddy bytecode proxies) |
| **Dirty Checking** | **NO** (Must call `repository.save(entity)`) | **YES** (Automatic flush on commit) |
| **First-Level Cache** | **NO** (Every query hits the database) | **YES** (Entity identity map in PersistenceContext) |
| **N+1 Query Risk** | Low (Queries are explicit) | **High** (Traversing lazy relationships) |
| **SQL Predictability** | **100% Deterministic** | Variable (Depends on dirty checking & flush order) |
| **Ideal Use Case** | Microservices, High-throughput systems, CQRS | Complex rich domain models, Enterprise monoliths |

---

## 4. Production Example in Java 21: Spring Data JDBC Aggregate Root
```java
package com.spring.interview.jdbc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

@Table("orders")
public record OrderAggregate(
    @Id Long id,
    String customerEmail,
    double totalAmount,
    Instant createdAt,
    Set<OrderItem> items
) {
    @Table("order_items")
    public record OrderItem(
        @Id Long id,
        String productSku,
        int quantity,
        double price
    ) {}
}
```

---

## 5. Common Mistakes
- **Expecting automatic updates without `repository.save()` in Spring Data JDBC**: Spring Data JDBC has no dirty checking; changes made to an entity in memory are not saved to the database unless `save()` is explicitly invoked.

---

## 6. Interview Questions
1. **SDE2**: What is the primary architectural difference between Spring Data JDBC and Spring Data JPA?
2. **Senior**: When would you choose Spring Data JDBC over Hibernate in high-throughput cloud microservices?

---

## 7. Interview Answer (Senior Level)
"The primary architectural difference is that Spring Data JDBC eliminates the Hibernate `EntityManager` Session, Persistence Context, dynamic proxy lazy loading, and automatic dirty checking. It adheres to DDD Aggregate Roots: loading an aggregate loads all child items eagerly without proxies, and persisting requires an explicit `repository.save()`. In high-throughput cloud microservices, Spring Data JDBC is chosen when predictability, low latency, low memory footprint, and GraalVM AOT native compilation are paramount, avoiding the insidious N+1 query and dirty checking performance pitfalls of Hibernate."
