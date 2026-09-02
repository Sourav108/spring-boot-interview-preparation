# 10-01: Bean Validation (JSR-380), @Valid vs @Validated & Custom Validators

> **Module**: `MOD-10: Validation and Error Handling`
> **Topic ID**: `SB-10-01`
> **Prerequisites**: `SB-08-01`, `SB-09-01`
> **Primary Technology**: Java 21 LTS | Jakarta Bean Validation (Hibernate Validator) | Custom Constraints
> **Verification Date**: 2026-09-01

---

## 1. Problem
Manual validation with sprawling `if-else` blocks inside controllers clutters business logic, causes inconsistent error messages, and fails to handle nested collections or custom domain constraints (such as phone number formatting, tax IDs, or ISO country codes).

---

## 2. Why It Exists
Spring integrates **Jakarta Bean Validation 3.0 (JSR-380)** implemented by **Hibernate Validator**. Validations are declared declaratively via annotations and evaluated automatically before controller method execution.

---

## 3. Difference Between `@Valid` and `@Validated`

| Dimension | `@Valid` | `@Validated` |
|---|---|---|
| **Origin** | Standard Jakarta Bean Validation (`jakarta.validation.Valid`) | Spring Framework (`org.springframework.validation.annotation.Validated`) |
| **Location** | Method parameters, fields, nested object references | Class level, method parameters |
| **Cascade Validation** | **YES** (Validates nested child DTO objects) | No (Class level triggers Spring AOP proxy) |
| **Validation Groups** | **NO** (Validates `Default.class` group only) | **YES** (Supports validation groups: `@Validated(Create.class)`) |
| **Service Layer Method Validation**| Requires `@Validated` on the service class | **Enables AOP method validation via `MethodValidationPostProcessor`** |

---

## 4. Production Example in Java 21: Custom Constraint Validator

### 1. The Constraint Annotation
```java
package com.spring.interview.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "Invalid international phone number format (must match E.164 e.g. +14155552671)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### 2. The ConstraintValidator Implementation
```java
package com.spring.interview.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // E.164 International Phone Regex
    private static final Pattern E164_PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Let @NotBlank handle null checks if required
        }
        return E164_PATTERN.matcher(value.trim()).matches();
    }
}
```

---

## 5. Common Mistakes
- **Forgetting `@Valid` on nested object properties in DTOs**: Without `@Valid` on the child field, annotations inside the child object (`@NotBlank`) are silently ignored.

---

## 6. Interview Questions
1. **SDE2**: What is the difference between `@Valid` and `@Validated` in Spring Boot?
2. **Senior**: How does Spring perform validation on service layer methods outside the Spring MVC controller layer?

---

## 7. Interview Answer (Senior Level)
"`@Valid` is the standard Jakarta annotation used on controller arguments and nested DTO fields to trigger recursive cascaded validation. `@Validated` is a Spring annotation used at the class level to enable Spring AOP-based method parameter validation (via `MethodValidationPostProcessor`) and to specify validation groups (`@Validated(OnCreate.class)`). On service-layer methods, placing `@Validated` on the class and constraints (`@NotNull`, `@Min`) on method parameters causes Spring to create an AOP proxy that intercepts calls and throws a `ConstraintViolationException` if arguments fail validation."
