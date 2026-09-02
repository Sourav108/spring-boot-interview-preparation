# 30-03: Spring Data JPA & Hibernate Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-03`
> **Primary Technology**: Spring Data JPA 3.4 | Hibernate 6.6
> **Verification Date**: 2026-09-01

---

## 🚀 Key Rules & Best Practices
1. **N+1 Prevention**:
   ```java
   @Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
   Optional<User> findUserWithOrders(@Param("id") Long id);
   ```
2. **Read-Only Performance Optimization**:
   ```java
   @Transactional(readOnly = true) // Disables Hibernate dirty-checking snapshot overhead!
   ```
3. **Bi-Directional Relationship Helpers**: Always maintain both sides of `@OneToMany` / `@ManyToOne` in helper methods (`addChild`, `removeChild`).
4. **Stable `equals()` & `hashCode()`**: Base on business natural keys (e.g. `email` or `UUID`), NOT on nullable auto-generated `@Id`.
