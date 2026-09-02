# Module 12: Spring Data JPA & Hibernate

> **Module Code**: `MOD-12`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Hibernate 6.6 | Spring Data JPA | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into Spring Data JPA and Hibernate internals: `EntityManager` and Persistence Context first-level cache mechanics, the 4 entity lifecycle states (Transient, Persistent, Detached, Removed), automatic snapshot dirty checking, resolving the N+1 query problem using 4 production strategies (JPQL `JOIN FETCH`, `@EntityGraph`, DTO Record Projections, and `default_batch_fetch_size`), bi-directional relationship synchronization, stable entity `equals()`/`hashCode()` identity contracts, dynamic `Specification` compositions, and high-throughput Java 21 Record constructor projections (`SELECT new ...`).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-12-01** | [`01-jpa-hibernate-persistence-context-and-dirty-checking.md`](./01-jpa-hibernate-persistence-context-and-dirty-checking.md) | Persistence Context identity map, 4 entity states, and snapshot array dirty checking. |
| **SB-12-02** | [`02-n-plus-one-query-problem-detection-and-four-remediations.md`](./02-n-plus-one-query-problem-detection-and-four-remediations.md) | N+1 query mechanics, detection statistics, and the 4 solutions (`JOIN FETCH`, `@EntityGraph`, DTOs, batch size). |
| **SB-12-03** | [`03-bidirectional-relationships-and-equals-hashcode-contract.md`](./03-bidirectional-relationships-and-equals-hashcode-contract.md) | Owning vs inverse sides, defensive helper methods, and avoiding Lombok `@Data` hashCode recursion bugs. |
| **SB-12-04** | [`04-spring-data-repositories-query-methods-and-specifications.md`](./04-spring-data-repositories-query-methods-and-specifications.md) | Startup query derivation AST validation, `@Query`, and Criteria API `Specification` composition. |
| **SB-12-05** | [`05-jpa-projections-interface-record-and-constructor.md`](./05-jpa-projections-interface-record-and-constructor.md) | Interface proxies vs Java 21 Record constructor projections (`SELECT new ...`) with zero dirty checking. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/jpa/`](./src/main/java/com/spring/interview/jpa/):

```
12-spring-data-jpa-and-hibernate/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/jpa/
    │   ├── dto/UserSummaryProjection.java                       # Interface view and Java 21 Record projections
    │   ├── entity/OrderEntity.java                              # Owning side entity (@ManyToOne, stable equals/hashCode)
    │   ├── entity/UserEntity.java                               # Inverse side entity with defensive addOrder/removeOrder
    │   ├── repository/UserRepository.java                       # Repository with JOIN FETCH, @EntityGraph, Record projections
    │   └── SpringDataJpaApplication.java                        # Executable application entrypoint
    └── test/java/com/spring/interview/jpa/                      # 100% Mocked Tier Test Suite (3 SpringBootTest Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
