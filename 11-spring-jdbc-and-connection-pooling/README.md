# Module 11: Spring JDBC & Connection Pooling

> **Module Code**: `MOD-11`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring JDBC | HikariCP Connection Pool | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master low-level relational database access and connection pool physics: `JdbcTemplate` and `NamedParameterJdbcTemplate` resource lifecycle, `RowMapper` vs `ResultSetExtractor` vs `RowCallbackHandler`, batch insert performance, `javax.sql.DataSource` logical connection proxying, HikariCP bytecode micro-optimizations (`ConcurrentBag`, `FastList`), PostgreSQL pool sizing formula $PoolSize = (Cores \times 2) + Spindles$, diagnosing connection leaks via `leak-detection-threshold`, and comparing Spring Data JDBC vs Spring Data JPA.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-11-01** | [`01-jdbctemplate-and-namedparameterjdbctemplate-internals.md`](./01-jdbctemplate-and-namedparameterjdbctemplate-internals.md) | Template Method pattern, `RowMapper` (1:1), `ResultSetExtractor` (1:N), and batch updates. |
| **SB-11-02** | [`02-datasource-architecture-and-connection-lifecycle.md`](./02-datasource-architecture-and-connection-lifecycle.md) | Physical TCP connection costs, `DataSource` SPI, and `HikariProxyConnection` return semantics. |
| **SB-11-03** | [`03-hikaricp-internals-fastpath-and-pool-sizing.md`](./03-hikaricp-internals-fastpath-and-pool-sizing.md) | `ConcurrentBag` lock-free queue, CPU context switching elimination, and pool sizing formulas. |
| **SB-11-04** | [`04-connection-leaks-timeouts-and-pool-exhaustion.md`](./04-connection-leaks-timeouts-and-pool-exhaustion.md) | Root causes of pool exhaustion, `leak-detection-threshold`, and stack trace capture. |
| **SB-11-05** | [`05-spring-data-jdbc-vs-spring-data-jpa.md`](./05-spring-data-jdbc-vs-spring-data-jpa.md) | DDD Aggregate Roots, zero-cache determinism in Spring Data JDBC vs JPA ORM magic. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/jdbc/`](./src/main/java/com/spring/interview/jdbc/):

```
11-spring-jdbc-and-connection-pooling/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/jdbc/
    │   ├── repository/UserJdbcRepository.java                   # NamedParameterJdbcTemplate with batch updates
    │   ├── pool/HikariPoolMetricsInspector.java                 # HikariPoolMXBean runtime metrics inspector
    │   └── SpringJdbcApplication.java                           # Executable application entrypoint
    └── test/java/com/spring/interview/jdbc/                     # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
