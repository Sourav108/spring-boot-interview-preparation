# 14-05: Flyway Callbacks, Java-Based Migrations & CI/CD Pipelines

> **Module**: `MOD-14: Database Migrations`
> **Topic ID**: `SB-14-05`
> **Prerequisites**: `SB-14-01`, `SB-14-02`
> **Primary Technology**: Java 21 LTS | Flyway SPI | Programmatic Migrations & CI/CD
> **Verification Date**: 2026-09-01

---

## 1. Problem
Some migrations cannot be expressed in pure SQL: encrypting existing sensitive passwords with BCrypt, reading historical BLOB data from S3 to backfill columns, or triggering telemetry webhooks when a database migration completes.

---

## 2. Why It Exists
Flyway provides two programmatic extension mechanisms:
1. **Java-based Migrations (`BaseJavaMigration`)**: Java classes executing arbitrary programmatic logic alongside standard SQL migrations.
2. **Flyway Lifecycle Callbacks (`Callback` / `CallbackExtension`)**: Event listeners intercepting migration phases (`beforeMigrate`, `afterEachMigrate`, `afterMigrate`, `handleValidationErrors`).

---

## 3. Production Example in Java 21: `BaseJavaMigration`
```java
package com.spring.interview.migration.java;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;

public class V1_1__PopulateDefaultAdminUser extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        String sql = "INSERT INTO users (id, username, email, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {
            statement.setString(1, "usr-admin-default");
            statement.setString(2, "admin");
            statement.setString(3, "security-admin@system.local");
            statement.setString(4, "ACTIVE");
            statement.executeUpdate();
        }
    }
}
```

---

## 4. Production Example in Java 21: Flyway Lifecycle Callback
```java
package com.spring.interview.migration.callback;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditMigrationCallback implements Callback {

    private static final Logger log = LoggerFactory.getLogger(AuditMigrationCallback.class);

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.BEFORE_MIGRATE || event == Event.AFTER_MIGRATE;
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return false;
    }

    @Override
    public void handle(Event event, Context context) {
        if (event == Event.BEFORE_MIGRATE) {
            log.info("FLYWAY EVENT: Starting database schema migration verification...");
        } else if (event == Event.AFTER_MIGRATE) {
            log.info("FLYWAY EVENT: Database schema migration completed successfully! ✅");
        }
    }

    @Override
    public String getCallbackName() {
        return "AuditMigrationCallback";
    }
}
```

---

## 5. Common Mistakes
- **Writing long-running CPU loops in Java migrations**: Blocks container startup and risks deployment timeouts; keep data seeding bounded or execute via background jobs.

---

## 6. Interview Questions
1. **SDE2**: What is a Java-based Flyway migration and when should you use it over a `.sql` file?
2. **Senior**: How do you prevent Flyway from running automatically on production pod startup in Kubernetes (e.g. running migrations as an InitContainer / K8s Job)?

---

## 7. Interview Answer (Senior Level)
"Java-based migrations extend `BaseJavaMigration` and are used when migration logic requires cryptographic operations, third-party API calls, or complex programmatic data transformations that cannot be expressed in standard SQL. In Kubernetes production environments, letting 50 concurrent application pods execute Flyway on startup is an anti-pattern. Senior architects disable startup migrations (`spring.flyway.enabled=false`) and execute Flyway migrations inside a dedicated **Kubernetes Job / InitContainer** before deploying the application pods, ensuring the database migration completes atomically before application traffic routes to new pods."
