# 15-03: Authorization Architecture: AuthorizationFilter & Method Security (@PreAuthorize)

> **Module**: `MOD-15: Spring Security`
> **Topic ID**: `SB-15-03`
> **Prerequisites**: `SB-15-01`, `SB-15-02`
> **Primary Technology**: Java 21 LTS | Method Security | SpEL Expression Evaluation
> **Verification Date**: 2026-09-01

---

## 1. Problem
URL-based access control (`requestMatchers("/admin/**").hasRole("ADMIN")`) is insufficient when authorization rules depend on method arguments, resource ownership (e.g. `order.ownerId == principal.userId`), or domain business logic.

---

## 2. Why It Exists
Spring Security 6 replaces legacy `AccessDecisionManager` with **`AuthorizationManager`** and provides `@EnableMethodSecurity` for fine-grained, AOP-based method security annotations evaluated via Spring Expression Language (SpEL):
- **`@PreAuthorize`**: Evaluates SpEL expression *before* method execution (access control, argument validation).
- **`@PostAuthorize`**: Evaluates SpEL expression *after* method execution (inspects method return value: `returnObject.owner == authentication.name`).
- **`@Secured` / `@RolesAllowed`**: Legacy role-based markers.

---

## 3. Architecture: Method Security AOP Interception

```mermaid
flowchart TD
    Call["Caller invokes orderService.deleteOrder(orderId)"] --> Proxy["Spring AOP Method Security Proxy"]
    Proxy --> PreAuth["MethodSecurityInterceptor / AuthorizationManager"]
    PreAuth --> SpEL["Evaluate SpEL: @PreAuthorize(\"hasRole('ADMIN') or #orderId == principal.id\")"]

    SpEL --> Decision{"Is Expression True?"}
    Decision -- "No" --> Deny["Throw AccessDeniedException (403 Forbidden) 🛑"]
    Decision -- "Yes" --> Target["Execute real service method ✅"]
```

---

## 4. Production SpEL Expressions in Java 21
```java
package com.spring.interview.security.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DocumentManagementService {

    public record Document(String id, String ownerUsername, String confidentialData) {}

    // Pre-execution authorization checking user role or parameter match
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public void deleteUserDocuments(String username) {
        // Business logic
    }

    // Post-execution authorization inspecting returned object
    @PostAuthorize("returnObject.ownerUsername == authentication.name or hasRole('AUDITOR')")
    public Document getDocumentById(String documentId) {
        return new Document(documentId, "alice", "Secret Financial Statement");
    }
}
```

---

## 5. Common Mistakes
- **Forgetting `@EnableMethodSecurity` on configuration classes**: Without `@EnableMethodSecurity`, `@PreAuthorize` annotations are silently ignored at runtime!

---

## 6. Interview Questions
1. **SDE2**: What is the difference between `@PreAuthorize` and `@PostAuthorize`?
2. **Senior**: How does `@PreAuthorize` resolve method parameter names like `#username` using compiled bytecode parameter reflection?

---

## 7. Interview Answer (Senior Level)
"`@PreAuthorize` checks authorization rules before method invocation, whereas `@PostAuthorize` allows the method to execute and evaluates access against the return value (`returnObject`), throwing `AccessDeniedException` if the check fails. `@PreAuthorize` resolves parameter names via Spring's `DefaultSecurityParameterNameDiscoverer`. In Java 21, by enabling the `-parameters` compiler flag in Maven (`maven-compiler-plugin`), parameter names (`username`, `orderId`) are preserved in class bytecode, allowing SpEL evaluation contexts to bind `#username` directly without needing manual `@Param` annotations."
