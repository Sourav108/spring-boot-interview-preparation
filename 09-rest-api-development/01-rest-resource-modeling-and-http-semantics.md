# 09-01: REST Resource Modeling, HTTP Semantics & Status Codes

> **Module**: `MOD-09: REST API Development`
> **Topic ID**: `SB-09-01`
> **Prerequisites**: `SB-08-01`
> **Primary Technology**: Java 21 LTS | REST Principles | HTTP Verbs & Status Semantics
> **Verification Date**: 2026-09-01

---

## 1. Problem
Non-idiomatic REST APIs abuse HTTP methods (e.g. using `POST /getUser` or `GET /deleteUser`), return `200 OK` on errors with `{ "status": "error" }` in JSON, or fail to adhere to RFC HTTP idempotency and safety standards.

---

## 2. Why It Exists
REST (Representational State Transfer) is an architectural style based on **Resources** identified by nouns (`/users`, `/orders`) and manipulated using standard HTTP verbs with well-defined safety and idempotency semantics.

---

## 3. HTTP Methods: Safety & Idempotency Semantics

```
Safe:       Does NOT modify server state (read-only)
Idempotent: Executing N identical requests produces the SAME server state as executing 1 request
```

| HTTP Method | Resource URI Example | Purpose | Safe? | Idempotent? | Standard Success Status |
|---|---|---|:---:|:---:|:---:|
| **GET** | `/api/v1/users/42` | Retrieve resource | **YES** | **YES** | `200 OK` |
| **POST** | `/api/v1/users` | Create new resource | **NO** | **NO** | `201 Created` (`Location` header) |
| **PUT** | `/api/v1/users/42` | Full replacement/upsert | **NO** | **YES** | `200 OK` / `204 No Content` |
| **PATCH** | `/api/v1/users/42` | Partial update | **NO** | **NO** (Usually) | `200 OK` |
| **DELETE** | `/api/v1/users/42` | Delete resource | **NO** | **YES** | `204 No Content` / `200 OK` |
| **HEAD** | `/api/v1/users/42` | Headers only (no body) | **YES** | **YES** | `200 OK` |
| **OPTIONS** | `/api/v1/users` | CORS preflight check | **YES** | **YES** | `204 No Content` / `200 OK` |

---

## 4. Standard HTTP Status Codes in Spring REST
- **`200 OK`**: Successful GET, PUT, or PATCH.
- **`201 Created`**: Successful POST creation; must return `Location: /api/v1/users/{id}` response header.
- **`204 No Content`**: Successful DELETE or PUT with no body returned.
- **`400 Bad Request`**: Request validation error or malformed JSON.
- **`401 Unauthorized`**: Authentication missing or invalid (no valid Bearer token).
- **`403 Forbidden`**: Authenticated user lacks permission (insufficient RBAC role).
- **`404 Not Found`**: Target resource does not exist.
- **`409 Conflict`**: State conflict (e.g. duplicate email address or optimistic lock mismatch).
- **`422 Unprocessable Entity`**: Semantic business validation failure.
- **`500 Internal Server Error`**: Unexpected server-side bug.
- **`503 Service Unavailable`**: Downstream dependency unavailable (circuit breaker open).

---

## 5. Common Mistakes
- **Using verbs in URIs (`/createUser`, `/updateUser`)**: Anti-pattern! REST uses nouns for resources (`POST /users`, `PUT /users/{id}`).

---

## 6. Interview Questions
1. **SDE2**: What is the difference between `PUT` and `PATCH` in REST API design?
2. **Senior**: Why is `DELETE` considered idempotent even if the second request returns `404 Not Found`?

---

## 7. Interview Answer (Senior Level)
"Idempotency refers to **the resulting server state**, not the HTTP status code. After calling `DELETE /api/v1/users/42` once, user 42 is removed from the database. Calling `DELETE /api/v1/users/42` a second time leaves the database in the exact same state (user 42 remains absent), even though the API returns `204 No Content` on the first call and `404 Not Found` on the second. `PUT` represents a complete replacement of the target resource and is idempotent, whereas `PATCH` represents a delta/partial modification (such as appending to an array) which may not be idempotent."
