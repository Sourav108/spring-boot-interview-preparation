# 09-04: API Versioning Strategies: URI, Header & Media Type

> **Module**: `MOD-09: REST API Development`
> **Topic ID**: `SB-09-04`
> **Prerequisites**: `SB-09-01`
> **Primary Technology**: Java 21 LTS | API Lifecycle | Versioning Patterns
> **Verification Date**: 2026-09-01

---

## 1. Problem
As APIs evolve, breaking changes (removing fields, renaming endpoints, altering validation rules) must be introduced without breaking existing mobile apps or third-party integrations.

---

## 2. Why It Exists
Spring MVC supports three primary REST API versioning strategies:
1. **URI Path Versioning**: Embeds the version directly in the path (`/api/v1/users` vs `/api/v2/users`). **Most popular in industry**.
2. **Custom Request Header Versioning**: Passes the version in a header (`X-API-Version: 2`).
3. **Media Type / Content Negotiation Versioning**: Passes the version in the `Accept` header (`Accept: application/vnd.company.v2+json`).

---

## 3. Comparing Versioning Strategies

| Strategy | Example | Pros | Cons |
|---|---|---|---|
| **URI Path** | `GET /api/v1/users` | Simple, visible, easily cached by CDNs/browsers | Pollutes URI space |
| **Header** | `GET /api/users` (`X-API-Version: 2`) | Clean URIs | Cannot bookmark in browser; CDN caching requires `Vary` header |
| **Media Type** | `Accept: application/vnd.app.v2+json` | True RESTful HATEOAS adherence | Complex client setup, awkward testing via curl |

---

## 4. Production Example in Java 21: URI and Header Versioning in Spring MVC
```java
package com.spring.interview.rest.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionedApiController {

    // 1. URI Path Versioning (Recommended)
    @GetMapping("/api/v1/products")
    public String getProductsV1() {
        return "PRODUCTS_V1_BASIC";
    }

    @GetMapping("/api/v2/products")
    public String getProductsV2() {
        return "PRODUCTS_V2_ENRICHED";
    }

    // 2. Custom Header Versioning
    @GetMapping(value = "/api/orders", headers = "X-API-VERSION=1")
    public String getOrdersV1() {
        return "ORDERS_V1";
    }

    @GetMapping(value = "/api/orders", headers = "X-API-VERSION=2")
    public String getOrdersV2() {
        return "ORDERS_V2";
    }
}
```

---

## 5. Common Mistakes
- **Breaking changes without incrementing version**: Removing a field from a JSON response without notice breaks client apps.

---

## 6. Interview Questions
1. **SDE2**: What are the pros and cons of URI path versioning versus Header versioning?
2. **Senior**: How do you deprecate and sunset an older API version (e.g. v1) in a high-traffic production system?

---

## 7. Interview Answer (Senior Level)
"URI path versioning (`/api/v1/orders`) is the industry standard because it is explicit, easy to route at the API gateway tier, and natively cacheable by CDNs. Header versioning (`Accept` or `X-API-Version`) provides cleaner URIs but requires strict `Vary: X-API-Version` caching headers. To deprecate an API version: 1) Emit the RFC 8594 `Sunset` and `Deprecation` HTTP headers with a target decommissioning date, 2) Track usage per API client via telemetry, 3) Notify consumers with migration guides, and 4) Return `410 Gone` once the sunset deadline passes."
