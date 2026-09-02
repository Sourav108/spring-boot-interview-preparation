# 13-03: Transaction Isolation Levels & Concurrency Phenomena

> **Module**: `MOD-13: Transactions & Concurrency`
> **Topic ID**: `SB-13-03`
> **Prerequisites**: `SB-13-01`
> **Primary Technology**: Java 21 LTS | SQL Isolation Standards | Concurrency Phenomena
> **Verification Date**: 2026-09-01

---

## 1. Problem
When concurrent transactions read and write to the same database rows simultaneously, race conditions emerge: reading uncommitted dirty data, having data modified beneath an active transaction, or observing phantom rows inserted mid-query.

---

## 2. Why It Exists: The 3 Classic Concurrency Phenomena
1. **Dirty Read**: Transaction $A$ reads uncommitted modifications made by Transaction $B$. If $B$ rolls back, $A$ read phantom invalid data.
2. **Non-Repeatable Read**: Transaction $A$ reads a row. Transaction $B$ updates and commits that row. Transaction $A$ reads the row again and observes modified values.
3. **Phantom Read**: Transaction $A$ queries a range of rows (`WHERE balance > 100`). Transaction $B$ inserts a new row matching that range and commits. Transaction $A$ re-runs the range query and sees an extra "phantom" row.

---

## 3. The 4 Standard ANSI SQL Isolation Levels

| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read | PostgreSQL Implementation Notes |
|---|:---:|:---:|:---:|---|
| **`READ_UNCOMMITTED`** | **Permitted** | **Permitted** | **Permitted** | Treated as `READ COMMITTED` in PostgreSQL |
| **`READ_COMMITTED`** *(Default in PG/Oracle)* | **Prevented 🛡️** | **Permitted** | **Permitted** | Each query sees a new snapshot of committed data |
| **`REPEATABLE_READ`** *(Default in MySQL)* | **Prevented 🛡️** | **Prevented 🛡️** | **Prevented in PG! 🛡️**| Snapshot isolation across the entire transaction |
| **`SERIALIZABLE`** | **Prevented 🛡️** | **Prevented 🛡️** | **Prevented 🛡️** | Full Serializable Snapshot Isolation (SSI) |

---

## 4. Setting Isolation in Spring Boot
```java
@Transactional(isolation = Isolation.REPEATABLE_READ)
public FinancialReport generateQuarterlyFinancialReport(Long companyId) {
    // Guarantees all queries inside this transaction see a consistent snapshot!
}
```

---

## 5. Common Mistakes
- **Assuming `SERIALIZABLE` has no performance penalty**: Serializable isolation requires optimistic predicate locks or aborts on write conflicts, requiring robust client retry logic.

---

## 6. Interview Questions
1. **SDE2**: What is the difference between a Non-Repeatable Read and a Phantom Read?
2. **Senior**: How does PostgreSQL's MVCC (Multi-Version Concurrency Control) implement `REPEATABLE READ` without locking rows?

---

## 7. Interview Answer (Senior Level)
"A Non-Repeatable Read occurs when a single existing row is modified by another transaction between reads, whereas a Phantom Read occurs when new rows matching a range query are inserted by another transaction. In PostgreSQL, MVCC implements `REPEATABLE READ` using transaction snapshot identifiers (`xmin` and `xmax` tuple headers). At the start of a `REPEATABLE READ` transaction, Postgres takes a snapshot of all currently active transaction IDs. Every subsequent query in the transaction only reads row versions committed prior to that snapshot, completely eliminating Dirty, Non-Repeatable, and Phantom reads without acquiring reader locks."
