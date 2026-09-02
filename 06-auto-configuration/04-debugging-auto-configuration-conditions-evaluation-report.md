# 06-04: Debugging Auto-Configuration: ConditionEvaluationReport & --debug

> **Module**: `MOD-06: Auto-Configuration`
> **Topic ID**: `SB-06-04`
> **Prerequisites**: `SB-06-02`, `SB-06-03`
> **Primary Technology**: Java 21 LTS | Diagnostics | ConditionEvaluationReport
> **Verification Date**: 2026-09-01

---

## 1. Problem
Why did Spring Boot configure an in-memory H2 database instead of connecting to PostgreSQL? Why was a specific security filter chain created? Why did a custom starter fail to activate?

---

## 2. Why It Exists
Spring Boot provides the **`ConditionEvaluationReport`** (enabled by launching the application with `--debug` or `debug=true` in `application.properties`). It outputs an exhaustive, high-signal diagnostic breakdown divided into:
1. **Positive Matches (`CONDITIONS EVALUATION REPORT -> Positive matches`)**: Auto-configurations and beans that were enabled, along with the exact conditions that matched.
2. **Negative Matches (`Negative matches`)**: Auto-configurations that were skipped, with the exact reason why each condition failed.
3. **Exclusions**: Configurations explicitly excluded via `exclude = ...`.
4. **Unconditional Classes**: Configurations that run without conditions.

---

## 3. Architecture: Reading the Conditions Evaluation Report

```
============================
CONDITIONS EVALUATION REPORT
============================

Positive matches:
-----------------
   DataSourceAutoConfiguration matched:
      - @ConditionalOnClass found required class 'javax.sql.DataSource' (OnClassCondition)

   DataSourceAutoConfiguration#dataSource matched:
      - @ConditionalOnMissingBean (types: javax.sql.DataSource; SearchStrategy: all) did not find any beans (OnBeanCondition)

Negative matches:
-----------------
   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'jakarta.jms.ConnectionFactory' (OnClassCondition)

   SecurityAutoConfiguration:
      Did not match:
         - Excluded in @SpringBootApplication(exclude = SecurityAutoConfiguration.class)
```

---

## 4. Programmatic Access to `ConditionEvaluationReport`
In integration tests or diagnostic tools:

```java
ConditionEvaluationReport report = ConditionEvaluationReport.get(applicationContext.getBeanFactory());
Map<String, ConditionEvaluationReport.ConditionAndOutcomes> outcomes = report.getConditionAndOutcomesBySource();
```

---

## 5. Common Mistakes
- **Guessing why a bean wasn't created instead of running `--debug`**: Running with `--debug` gives the exact single condition line that evaluated to `false`.

---

## 6. Interview Questions
1. **SDE2**: How do you find out why Spring Boot did or did not create a specific auto-configured bean?
2. **Senior**: How does the `ConditionEvaluationReport` record evaluations without executing `@Bean` factory methods prematurely?

---

## 7. Interview Answer (Senior Level)
"To diagnose auto-configuration decisions, pass the `--debug` command-line argument or set `debug=true` in `application.properties`. Spring Boot generates a `ConditionEvaluationReport` containing Positive Matches (configurations activated because all `@Conditional` annotations matched) and Negative Matches (configurations skipped, stating the exact missing class, property, or existing bean). The report evaluates `@Conditional` metadata during the `BeanDefinitionRegistry` phase using ASM bytecode inspection without prematurely instantiating bean target instances."
