# 12-04: Spring Data Repositories, Query Methods & Specifications

> **Module**: `MOD-12: Spring Data JPA & Hibernate`
> **Topic ID**: `SB-12-04`
> **Prerequisites**: `SB-12-01`
> **Primary Technology**: Java 21 LTS | Query Generation | Criteria Specifications
> **Verification Date**: 2026-09-01

---

## 1. Problem
Building complex search filters (e.g. searching orders by status, date ranges, customer IDs, and minimum amounts where any filter parameter can be optional) leads to brittle string concatenation and SQL injection vulnerabilities.

---

## 2. Why It Exists
Spring Data JPA provides **Dynamic Query Derivation**, declarative `@Query` (JPQL and native SQL), and the **`JpaSpecificationExecutor<T>` / `Specification<T>`** pattern based on the JPA Criteria API.

---

## 3. Comparing Spring Data Query Mechanisms

| Mechanism | Example | Pros | Cons |
|---|---|---|---|
| **Derived Query Methods** | `findByEmailAndStatus(email, status)` | Zero SQL needed, rapid prototyping | Method names become unwieldy for >3 parameters |
| **Declarative `@Query` (JPQL)** | `@Query("SELECT u FROM User u WHERE u.email = :email")` | Database-agnostic, validated at application startup | Static query structure |
| **Native `@Query`** | `@Query(value = "SELECT * ...", nativeQuery = true)` | Leverage DB-specific features (Postgres JSONB, CTEs) | Tightly coupled to database vendor |
| **`Specification<T>`** | `repo.findAll(Specification.where(byStatus).and(byMinAmount))` | **100% Dynamic, type-safe, composable query predicates** | Requires JPA Criteria API fluency |

---

## 4. Production Example in Java 21: Composable Specification
```java
package com.spring.interview.jpa.specification;

import com.spring.interview.jpa.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    public static Specification<UserEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<UserEntity> emailContains(String term) {
        return (root, query, cb) -> term == null ? null : cb.like(cb.lower(root.get("email")), "%" + term.toLowerCase() + "%");
    }
}
```

---

## 5. Common Mistakes
- **Writing native queries with unvalidated string concatenation**: Always use named parameters `:param` to let Hibernate use parameterized `PreparedStatement`s.

---

## 6. Interview Questions
1. **SDE2**: When are derived query methods validated by Spring Data JPA?
2. **Senior**: How does `Specification<T>` enable building dynamic search filters without SQL concatenation?

---

## 7. Interview Answer (Senior Level)
"Spring Data derived query methods and `@Query` JPQL statements are validated at application startup during Spring context refresh: `RepositoryConfigurationDelegate` parses method names into an Abstract Syntax Tree (AST) and verifies entity property paths against JPA metadata, failing fast if a property does not exist. `Specification<T>` wraps the JPA Criteria API (`toPredicate()`), allowing developers to combine nullable predicates dynamically using `Specification.where().and()`. If a search parameter is null, the predicate evaluates to null and is ignored, generating clean SQL with zero string concatenation."
