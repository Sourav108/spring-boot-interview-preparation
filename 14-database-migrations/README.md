# Module 14: Database Migrations

> **Module Code**: `MOD-14`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Flyway | Zero-Downtime Expand/Contract | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master automated, version-controlled database schema evolution: comparing SQL-based Flyway vs XML/YAML Liquibase, Flyway naming grammar (`V__`, `U__`, `R__`), checksum validation and `flyway_schema_history` table internals, zero-downtime database migrations via the Expand and Contract pattern, disaster recovery and resolving failed migrations via `flyway:repair`, and extending Flyway with custom Java-based migrations (`BaseJavaMigration`) and lifecycle `Callback` hooks.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-14-01** | [`01-flyway-vs-liquibase-architecture-and-tradeoffs.md`](./01-flyway-vs-liquibase-architecture-and-tradeoffs.md) | Flyway vs Liquibase architectures, table lock acquisition, and `flyway_schema_history`. |
| **SB-14-02** | [`02-flyway-migration-versioning-naming-and-checksums.md`](./02-flyway-migration-versioning-naming-and-checksums.md) | File naming grammar, CRC32 checksum calculation, `FlywayValidateException`, and repair lifecycle. |
| **SB-14-03** | [`03-zero-downtime-database-migrations-expand-and-contract.md`](./03-zero-downtime-database-migrations-expand-and-contract.md) | Expand and Contract (Parallel Run) pattern: non-breaking dual-writes and gradual column phase-out. |
| **SB-14-04** | [`04-handling-failed-migrations-in-production-and-flyway-repair.md`](./04-handling-failed-migrations-in-production-and-flyway-repair.md) | Transactional DDL (PostgreSQL) vs non-transactional DDL (MySQL), disaster recovery with `flyway.repair()`. |
| **SB-14-05** | [`05-flyway-callbacks-java-migrations-and-cicd-pipelines.md`](./05-flyway-callbacks-java-migrations-and-cicd-pipelines.md) | Programmatic `BaseJavaMigration`, Flyway `Callback` event hooks, and Kubernetes InitContainers. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/migration/`](./src/main/java/com/spring/interview/migration/):

```
14-database-migrations/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/spring/interview/migration/
    │   │   ├── callback/AuditMigrationCallback.java             # Flyway Callback intercepting BEFORE/AFTER_MIGRATE
    │   │   ├── java/V1_1__PopulateDefaultAdminUser.java         # BaseJavaMigration seeding initial database users
    │   │   └── SpringMigrationApplication.java                  # Executable application entrypoint
    │   └── resources/
    │       ├── application.properties                           # Flyway multi-location classpath configuration
    │       └── db/migration/V1_0__create_user_schema.sql        # Standard SQL schema creation script
    └── test/java/com/spring/interview/migration/                # 100% Mocked Tier Test Suite (2 Integration Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
