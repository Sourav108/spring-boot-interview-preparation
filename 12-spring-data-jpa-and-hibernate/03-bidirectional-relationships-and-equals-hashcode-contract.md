# 12-03: Bi-directional Relationships, Cascade Types & The equals/hashCode Contract

> **Module**: `MOD-12: Spring Data JPA & Hibernate`
> **Topic ID**: `SB-12-03`
> **Prerequisites**: `SB-12-01`
> **Primary Technology**: Java 21 LTS | Hibernate 6.6 | Entity Identity Contract
> **Verification Date**: 2026-09-01

---

## 1. Problem
Two notorious Hibernate bugs plague production systems:
1. **Broken Bi-directional Synchronization**: Adding an order to `user.getOrders().add(order)` without setting `order.setUser(user)` leaves foreign keys `NULL` in the database.
2. **Lombok `@Data` HashCode Catastrophe**: `@Data` generates `equals()` and `hashCode()` using all fields including lazy child collections. Adding an entity to a `Set` triggers lazy loading or crashes with `StackOverflowError`.

---

## 2. Why It Exists
In JPA:
- The **owning side** of an association is the side that contains `@JoinColumn` (e.g. `@ManyToOne` on `OrderEntity`).
- The **inverse side** uses `mappedBy = "user"` on `@OneToMany`.
- Database foreign keys are *only* populated based on the state of the owning side!

---

## 3. The Gold-Standard Bi-directional Entity Pattern in Java 21

```java
package com.spring.interview.jpa.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    // Inverse side: mappedBy points to the "user" field in OrderEntity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>();

    // Defensive synchronization helper methods!
    public void addOrder(OrderEntity order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(OrderEntity order) {
        orders.remove(order);
        order.setUser(null);
    }

    // Hibernate-safe equals & hashCode (Based strictly on business key or persistent ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Constant hash code for entities without assigned natural ID
    }

    // Getters and Setters omitted for brevity
    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<OrderEntity> getOrders() { return orders; }
}
```

---

## 4. Common Mistakes
- **Using Lombok `@Data` or `@EqualsAndHashCode` on JPA Entities**: Triggers recursive getter calls across lazy bi-directional relationships, crashing JVMs with `StackOverflowError`.

---

## 5. Interview Questions
1. **SDE2**: Why is it important to implement defensive synchronization helper methods (`addOrder()` / `removeOrder()`) in bi-directional JPA mappings?
2. **Senior**: Why should JPA entity `hashCode()` return a constant `getClass().hashCode()` when entities use auto-generated database IDs?

---

## 6. Interview Answer (Senior Level)
"When an entity uses database-generated primary keys (e.g. `IDENTITY` or `SEQUENCE`), its `id` is `null` before `persist()` and assigned a value only after SQL execution. If `hashCode()` uses `id`, adding the transient entity to a Java `HashSet` stores it in the `null` hash bucket. Once persisted, its `id` changes, altering its hash code. Subsequent `set.contains(entity)` lookups fail because the entity is searched in a different bucket, violating the Java `Set` contract. Returning a constant `getClass().hashCode()` guarantees a stable hash code across the entire entity lifecycle while delegating uniqueness to `equals()`."
