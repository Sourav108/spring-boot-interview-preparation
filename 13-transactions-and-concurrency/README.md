# Module 13: Transactions & Concurrency

> **Module Code**: `MOD-13`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Transactions | ACID & Locking | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into Spring declarative and programmatic transaction management: tracing the AOP proxy boundary, `TransactionInterceptor`, `PlatformTransactionManager`, ThreadLocal state inside `TransactionSynchronizationManager`, after-commit event hooks, the 7 transaction propagation behaviors (`REQUIRED`, `REQUIRES_NEW`, `NESTED`), ANSI SQL isolation levels (Dirty, Non-Repeatable, and Phantom reads), rollback rules for checked vs unchecked exceptions (`rollbackFor = Exception.class`), fine-grained programmatic boundaries with `TransactionTemplate`, and Optimistic (`@Version`) vs Pessimistic (`PESSIMISTIC_WRITE`) concurrency control with deadlock elimination.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-13-01** | [`01-transactional-internals-and-transactionsynchronizationmanager.md`](./01-transactional-internals-and-transactionsynchronizationmanager.md) | Proxy boundary, `PlatformTransactionManager`, ThreadLocal state, `afterCommit()` hooks. |
| **SB-13-02** | [`02-transaction-propagation-mechanics-and-suspension.md`](./02-transaction-propagation-mechanics-and-suspension.md) | `REQUIRED` vs `REQUIRES_NEW` suspension physics, physical vs logical transactions, pool deadlocks. |
| **SB-13-03** | [`03-isolation-levels-phenomena-and-postgresql-engine.md`](./03-isolation-levels-phenomena-and-postgresql-engine.md) | Concurrency phenomena (Dirty/Non-Repeatable/Phantom reads), PostgreSQL MVCC snapshot isolation. |
| **SB-13-04** | [`04-rollback-rules-checked-exceptions-and-transactiontemplate.md`](./04-rollback-rules-checked-exceptions-and-transactiontemplate.md) | Checked exception rollback trap, `rollbackFor = Exception.class`, `TransactionTemplate` boundaries. |
| **SB-13-05** | [`05-optimistic-vs-pessimistic-locking-and-deadlock-prevention.md`](./05-optimistic-vs-pessimistic-locking-and-deadlock-prevention.md) | `@Version` optimistic locking + retry vs `PESSIMISTIC_WRITE`, consistent global lock ordering. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/transactions/`](./src/main/java/com/spring/interview/transactions/):

```
13-transactions-and-concurrency/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/transactions/
    │   ├── entity/BankAccountEntity.java                        # Account entity for atomic transfers
    │   ├── entity/InventoryItemEntity.java                      # Optimistic locking entity with @Version
    │   ├── repository/BankAccountRepository.java                # Spring Data JPA repository
    │   ├── repository/InventoryItemRepository.java              # Inventory repository
    │   ├── service/AccountTransferService.java                  # Declarative & TransactionTemplate transfer logic
    │   ├── service/OptimisticLockingInventoryService.java       # Inventory reservation with retry backoff
    │   └── SpringTransactionsApplication.java                   # Executable application entrypoint
    └── test/java/com/spring/interview/transactions/             # 100% Mocked Tier Test Suite (4 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
