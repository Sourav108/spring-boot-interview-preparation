# 13-05: Optimistic vs Pessimistic Locking & Deadlock Prevention

> **Module**: `MOD-13: Transactions & Concurrency`
> **Topic ID**: `SB-13-05`
> **Prerequisites**: `SB-13-01`, `SB-13-03`
> **Primary Technology**: Java 21 LTS | Locking Strategies | Concurrency Control
> **Verification Date**: 2026-09-01

---

## 1. Problem
Two concurrent users try to book the last available seat on a flight simultaneously. Without proper concurrency control, both transactions succeed, creating a catastrophic **Double-Booking Over-Allocation** bug.

---

## 2. Why It Exists: The Two Locking Strategies

```mermaid
flowchart TD
    Strategy{"Choose Concurrency Control Strategy"}

    Strategy -->|1. Optimistic Locking (@Version)| Opt["No DB row locks held during read! At commit: UPDATE ... WHERE id=? AND version=?. Throws OptimisticLockingFailureException on conflict. ⚡ FAST for Read-Heavy workloads."]

    Strategy -->|2. Pessimistic Locking (SELECT FOR UPDATE)| Pess["Acquires exclusive DB row lock immediately! Blocks all concurrent writers until transaction commits. 🛡️ SAFE for High-Contention inventory."]
```

---

## 3. Comprehensive Comparison Matrix

| Dimension | Optimistic Locking (`@Version`) | Pessimistic Locking (`@Lock(PESSIMISTIC_WRITE)`) |
|---|:---:|:---:|
| **Locking Mechanism** | In-application version check (`WHERE version = ?`) | Database row-level lock (`SELECT ... FOR UPDATE`) |
| **Database Locks Held?** | **NO** (Zero DB lock duration) | **YES** (Lock held for entire transaction lifetime) |
| **Concurrency Throughput** | **Extremely High** | Lower (Concurrent threads block/wait in line) |
| **Handling Collisions** | Throws `OptimisticLockingFailureException` (Requires Retry) | Threads wait for lock release |
| **Deadlock Risk** | Zero DB deadlocks | **High DB Deadlock Risk** if locking rows in different order |
| **Best Used For** | High read, low contention (User profiles, e-commerce catalog) | High write contention (Flash sales, inventory counters) |

---

## 4. Production Example in Java 21: Optimistic Locking with Retry Handling
```java
package com.spring.interview.transactions.service;

import com.spring.interview.transactions.entity.InventoryItemEntity;
import com.spring.interview.transactions.repository.InventoryItemRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockingInventoryService {

    private final InventoryItemRepository repository;

    public OptimisticLockingInventoryService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    public boolean reserveItemWithRetry(Long itemId, int quantity, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return executeReservation(itemId, quantity);
            } catch (OptimisticLockingFailureException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw e;
                }
                // Exponential jitter backoff
                try { Thread.sleep(10 * attempts); } catch (InterruptedException ignored) {}
            }
        }
        return false;
    }

    @Transactional
    public boolean executeReservation(Long itemId, int quantity) {
        InventoryItemEntity item = repository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getAvailableStock() < quantity) {
            return false;
        }

        item.setAvailableStock(item.getAvailableStock() - quantity);
        repository.save(item);
        return true;
    }
}
```

---

## 5. Deadlock Prevention Rule
**Always acquire database row locks in a strictly consistent global order!**
If Transaction 1 locks Account A then Account B, while Transaction 2 locks Account B then Account A, the database deadlocks.
*Rule*: Sort account IDs numerically before acquiring locks (`if (acc1.id < acc2.id) lock(acc1); lock(acc2);`).

---

## 6. Interview Questions
1. **SDE2**: How does `@Version` prevent lost updates in JPA?
2. **Senior**: How do you design an account transfer system to be 100% immune to database deadlocks under high concurrency?

---

## 7. Interview Answer (Senior Level)
"`@Version` adds an integer or timestamp column to the entity. When updating, Hibernate executes `UPDATE table SET val=?, version=version+1 WHERE id=? AND version=?`. If another transaction modified the row concurrently, the `WHERE version=?` condition matches zero rows, causing Hibernate to throw `OptimisticLockingFailureException`. To prevent database deadlocks in multi-row locking operations (like transferring between two accounts), we enforce a global lock acquisition order: we sort account IDs numerically (`Math.min(idA, idB)` first) before acquiring pessimistic locks (`PESSIMISTIC_WRITE`). Because all threads lock rows in identical order, cyclic lock wait graphs cannot form, eliminating database deadlocks."
