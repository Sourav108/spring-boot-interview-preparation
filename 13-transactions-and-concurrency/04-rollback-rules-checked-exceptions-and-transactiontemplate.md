# 13-04: Rollback Rules, Checked Exceptions & Programmatic TransactionTemplate

> **Module**: `MOD-13: Transactions & Concurrency`
> **Topic ID**: `SB-13-04`
> **Prerequisites**: `SB-13-01`
> **Primary Technology**: Java 21 LTS | Transaction Rollback Rules | Programmatic Transactions
> **Verification Date**: 2026-09-01

---

## 1. Problem
A developer throws a checked exception (`throw new InsufficientFundsException("Balance too low")`) inside a `@Transactional` method. The method throws the exception, but **the database transaction commits anyway!** Why?

---

## 2. Why It Exists
By default, Spring's declarative transaction infrastructure **ONLY rolls back on unchecked exceptions (`RuntimeException` and `Error`)**. Checked exceptions (`Exception`) are treated as unexpected return values rather than transaction failures unless explicitly declared via `rollbackFor`.

---

## 3. The Core Rollback Rule & Solution

```java
// Anti-Pattern: Checked exception will NOT trigger rollback!
@Transactional
public void transferMoney() throws InsufficientFundsException { ... }

// Best Practice: Explicitly configure rollbackFor
@Transactional(rollbackFor = Exception.class)
public void transferMoney() throws InsufficientFundsException { ... }
```

---

## 4. Programmatic Transactions via `TransactionTemplate`
When you need fine-grained transaction boundaries (e.g. keeping external network calls OUTSIDE the transaction while keeping DB queries INSIDE):

```java
package com.spring.interview.transactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class OrderCheckoutService {

    private final TransactionTemplate transactionTemplate;

    public OrderCheckoutService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void processCheckout(String orderId) {
        // 1. External Network Call (Takes 3 seconds - OUTSIDE DB TRANSACTION!)
        String paymentToken = callExternalPaymentGateway();

        // 2. Fine-grained Database Mutation (Takes 5ms - INSIDE DB TRANSACTION!)
        transactionTemplate.execute(status -> {
            updateOrderStateInDatabase(orderId, paymentToken);
            decrementInventoryInDatabase(orderId);
            return true;
        });

        // 3. Send Email Notification (OUTSIDE DB TRANSACTION!)
        sendEmailConfirmation();
    }

    private String callExternalPaymentGateway() { return "TOKEN_OK"; }
    private void updateOrderStateInDatabase(String id, String token) {}
    private void decrementInventoryInDatabase(String id) {}
    private void sendEmailConfirmation() {}
}
```

---

## 5. Common Mistakes
- **Wrapping entire long-running batch processes in `@Transactional`**: Causes connection pool exhaustion and long database locks; use `TransactionTemplate` per small batch instead.

---

## 6. Interview Questions
1. **SDE2**: Why does Spring `@Transactional` default to rolling back only on `RuntimeException`?
2. **Senior**: When should you use `TransactionTemplate` instead of declarative `@Transactional`?

---

## 7. Interview Answer (Senior Level)
"Spring follows EJB design conventions where checked exceptions represent predictable business return paths (recoverable by the caller), while unchecked `RuntimeException`s represent catastrophic unrecoverable faults. To ensure all exceptions trigger rollback, senior engineers explicitly annotate `@Transactional(rollbackFor = Exception.class)`. `TransactionTemplate` is preferred over declarative `@Transactional` when methods perform slow I/O (like calling Stripe or generating PDFs) and only require transactional guarantees for a discrete subset of queries, preventing database connections from being held idle during network round-trips."
