# Module 10: Validation and Error Handling

> **Module Code**: `MOD-10`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Jakarta Validation (JSR-380) | RFC 7807 Problem Details | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into enterprise validation and error handling: Jakarta Bean Validation 3.0 (Hibernate Validator), `@Valid` vs `@Validated`, writing custom `ConstraintValidator`s (e.g. E.164 phone formatting), validation groups (`OnCreate` vs `OnUpdate`), `@RestControllerAdvice` and `@ExceptionHandler` resolution inheritance rules, RFC 7807 / RFC 9457 `ProblemDetail` standardization, and enforcing an 8-tier error taxonomy with strict security sanitization to prevent stack trace and SQL information disclosure vulnerabilities.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-10-01** | [`01-bean-validation-jsr-380-and-custom-validators.md`](./01-bean-validation-jsr-380-and-custom-validators.md) | Jakarta Bean Validation, `@Valid` vs `@Validated`, custom `@ValidPhoneNumber` constraint. |
| **SB-10-02** | [`02-validation-groups-and-conditional-constraints.md`](./02-validation-groups-and-conditional-constraints.md) | Multi-stage validation groups (`OnCreate` vs `OnUpdate`), `@GroupSequence`, and constraint inheritance. |
| **SB-10-03** | [`03-controlleradvice-and-exceptionhandler-architecture.md`](./03-controlleradvice-and-exceptionhandler-architecture.md) | `@RestControllerAdvice`, `ExceptionHandlerExceptionResolver`, inheritance tree resolution rules. |
| **SB-10-04** | [`04-rfc-7807-problem-details-error-standardization.md`](./04-rfc-7807-problem-details-error-standardization.md) | RFC 7807 / RFC 9457 `ProblemDetail` standard fields, content negotiation (`application/problem+json`). |
| **SB-10-05** | [`05-enterprise-error-taxonomy-and-security-sanitization.md`](./05-enterprise-error-taxonomy-and-security-sanitization.md) | 8-tier enterprise error taxonomy, correlation reference IDs, and stripping database stack traces. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/validation/`](./src/main/java/com/spring/interview/validation/):

```
10-validation-and-error-handling/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/validation/
    │   ├── controller/SampleValidationController.java           # Controller demonstrating validation & error flows
    │   ├── dto/CustomerRegistrationDto.java                     # Record DTO with @ValidPhoneNumber & constraints
    │   ├── exception/DomainExceptions.java                      # ResourceNotFoundException & BusinessConflictException
    │   ├── exception/GlobalExceptionHandler.java                # RFC 7807 @RestControllerAdvice with security sanitization
    │   ├── validator/PhoneNumberValidator.java                  # Custom E.164 phone regex ConstraintValidator
    │   ├── validator/ValidPhoneNumber.java                      # Custom Jakarta Constraint annotation
    │   └── SpringValidationApplication.java                     # Executable application entrypoint
    └── test/java/com/spring/interview/validation/               # 100% Mocked Tier Test Suite (5 MockMvc Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
