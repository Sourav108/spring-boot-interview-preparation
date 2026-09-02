# 10-04: RFC 7807 Problem Details: Standardized API Error Contracts

> **Module**: `MOD-10: Validation and Error Handling`
> **Topic ID**: `SB-10-04`
> **Prerequisites**: `SB-10-03`
> **Primary Technology**: Java 21 LTS | RFC 7807 / RFC 9457 | Standardized Error Payloads
> **Verification Date**: 2026-09-01

---

## 1. Problem
Every microservice team historically invented their own ad-hoc error JSON format (`{ "errorCode": 102, "msg": "failed" }` vs `{ "error": { "code": "NOT_FOUND" } }`), making client-side SDK error handling fragmented and inconsistent across the organization.

---

## 2. Why It Exists
**RFC 7807 (updated by RFC 9457)** defines a standardized, universal HTTP Problem Details JSON schema (`application/problem+json`). In **Spring Boot 3.x and Spring Framework 6.x**, `org.springframework.http.ProblemDetail` is a first-class framework citizen.

---

## 3. RFC 7807 Standard Specification Fields

```json
{
  "type": "https://api.example.com/errors/invalid-order-state",
  "title": "Invalid Order State Transition",
  "status": 409,
  "detail": "Order ORD-9912 is already CANCELLED and cannot be modified.",
  "instance": "/api/v1/orders/ORD-9912/cancel",
  "timestamp": "2026-09-01T12:00:00Z",
  "invalidParams": [
    {
      "field": "amount",
      "reason": "Amount must be positive"
    }
  ]
}
```

| Field | Type | RFC Description |
|---|---|---|
| **`type`** | URI | A URI reference identifying the specific error type |
| **`title`** | String | A short, human-readable summary of the problem type |
| **`status`** | Integer | The HTTP status code (e.g. 400, 404, 409, 500) |
| **`detail`** | String | A human-readable explanation specific to this occurrence |
| **`instance`** | URI | A URI identifying the specific request/resource path |
| *Custom Extensions* | Object | Arbitrary properties (`timestamp`, `traceId`, `invalidParams`) |

---

## 4. Enabling RFC 7807 in Spring Boot 3
In `application.properties`:

```properties
# Enables Spring Boot's built-in ProblemDetail responses for all standard exceptions
spring.mvc.problemdetails.enabled=true
```

---

## 5. Production Example in Java 21: Creating `ProblemDetail` Programmatically
```java
ProblemDetail problem = ProblemDetail.forStatusAndDetail(
    HttpStatus.BAD_REQUEST,
    "The requested transaction exceeds the daily transfer limit of $10,000"
);
problem.setTitle("Transaction Limit Exceeded");
problem.setType(URI.create("https://bank.example.com/errors/limit-exceeded"));
problem.setProperty("limit", 10000);
problem.setProperty("attemptedAmount", 15000);
problem.setProperty("timestamp", Instant.now().toString());
```

---

## 6. Common Mistakes
- **Inventing proprietary JSON error models in Spring Boot 3**: Use standard `ProblemDetail` instead of bespoke `ErrorResponse` classes.

---

## 7. Interview Questions
1. **SDE2**: What is RFC 7807 Problem Details and why is it standard in Spring Boot 3?
2. **Senior**: How do you enrich `ProblemDetail` with validation field errors and distributed trace IDs without leaking internal stack traces?

---

## 8. Interview Answer (Senior Level)
"RFC 7807 (Problem Details for HTTP APIs) is the standard JSON specification for HTTP error responses. Spring Framework 6 / Spring Boot 3 introduced `ProblemDetail` as the standard return type for error responses. To enrich it, we catch `MethodArgumentNotValidException` in `@RestControllerAdvice`, extract field-level errors into an `invalidParams` list, attach distributed tracing IDs (e.g. OpenTelemetry traceId) via `problem.setProperty("traceId", ...)`, and return `application/problem+json`, ensuring clean, standardized errors without leaking raw stack traces."
