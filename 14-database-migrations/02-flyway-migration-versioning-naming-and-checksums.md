# 14-02: Flyway Naming Conventions, Checksums & The Repair Lifecycle

> **Module**: `MOD-14: Database Migrations`
> **Topic ID**: `SB-14-02`
> **Prerequisites**: `SB-14-01`
> **Primary Technology**: Java 21 LTS | Flyway | Checksums & Validation
> **Verification Date**: 2026-09-01

---

## 1. Problem
A developer edits an already-applied migration script (`V1__create_users.sql`) to add a missing column. When the application starts up in staging, Flyway crashes with:
`FlywayValidateException: Migration checksum mismatch for migration version 1`.

---

## 2. Why It Exists
Flyway calculates a **CRC32 / SHA-256 checksum** for every migration file upon application. If a previously-applied file is modified in git, Flyway detects that the database state no longer matches the codebase and **aborts startup immediately** to prevent schema corruption.

---

## 3. Flyway File Naming Grammar

```
Prefix + Version + Separator + Description + Suffix
   V   +   1_2   +    __     + add_users   +  .sql
```

| Type | Prefix | Example File Name | Execution Semantics |
|---|:---:|---|---|
| **Versioned** | `V` | `V1_0__create_user_table.sql` | Executed **EXACTLY ONCE** in strict version order |
| **Undo** | `U` | `U1_0__drop_user_table.sql` | Rolls back the corresponding Versioned migration |
| **Repeatable** | `R` | `R__recreate_views.sql` | Re-executed **whenever its checksum changes** (for views/procedures) |

---

## 4. Resolving Checksum Mismatches: The Repair Lifecycle
Never edit an already-applied migration in production!
- **If the change was accidental**: Revert the local file back to its original applied contents.
- **If the change was intentional in local dev**: Run `mvn flyway:repair` or `flyway.repair()` to update the checksum in `flyway_schema_history`.
- **In Production**: Always write a **NEW** migration file (e.g. `V1_1__add_phone_to_users.sql`)!

---

## 5. Common Mistakes
- **Using a single underscore `_` instead of double underscore `__` in filename**: Flyway ignores the separator and fails to parse the version number.

---

## 6. Interview Questions
1. **SDE2**: What happens when a developer modifies a Flyway SQL migration script that was already committed and executed?
2. **Senior**: How do Repeatable Migrations (`R__`) differ from Versioned Migrations (`V__`), and what are they used for?

---

## 7. Interview Answer (Senior Level)
"When an already-applied migration file is modified, Flyway calculates its new checksum during the startup validation phase and compares it against the recorded checksum in `flyway_schema_history`. Because they differ, it throws `FlywayValidateException` and aborts container startup to prevent schema drift. Versioned migrations (`V__`) run exactly once in ascending version order and must be immutable once deployed. Repeatable migrations (`R__`) have no version number and re-execute automatically whenever their file checksum changes, making them ideal for managing idempotent database objects like views, stored procedures, and triggers."
