# Module 03: Bean Lifecycle and Configuration

> **Module Code**: `MOD-03`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Framework 6.2.2 | Spring Boot 3.4.13 | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Deep dive into the Spring bean creation lifecycle and configuration architecture: parsing `BeanDefinition` metadata warehouse, the 11-step lifecycle sequence from instantiation to destruction, custom `BeanPostProcessor` extension hooks and AOP proxy generation, bean scopes (`singleton`, `prototype`, `request`, `session`), resolving the Singleton-to-Prototype injection trap via `ObjectProvider`, `*Aware` callback interfaces, `FactoryBean<T>` complex instantiation, and `ApplicationEventPublisher` in-memory event-driven decoupling.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-03-01** | [`01-beandefinition-and-metadata-registry.md`](./01-beandefinition-and-metadata-registry.md) | `BeanDefinition` metadata registry, programmatic bean definition, and `BeanFactoryPostProcessor`. |
| **SB-03-02** | [`02-complete-bean-lifecycle-instantiation-to-destruction.md`](./02-complete-bean-lifecycle-instantiation-to-destruction.md) | 11-step lifecycle flow, execution ordering of `@PostConstruct`, `afterPropertiesSet()`, and destroy hooks. |
| **SB-03-03** | [`03-beanpostprocessor-and-custom-initialization.md`](./03-beanpostprocessor-and-custom-initialization.md) | `BeanPostProcessor` extension mechanics, annotation parsing, and AOP auto-proxy creation. |
| **SB-03-04** | [`04-bean-scopes-singleton-prototype-web-scopes.md`](./04-bean-scopes-singleton-prototype-web-scopes.md) | Bean scopes, solving the Singleton-to-Prototype injection dilemma with `ObjectProvider` & `@Lookup`. |
| **SB-03-05** | [`05-aware-interfaces-factorybean-and-application-events.md`](./05-aware-interfaces-factorybean-and-application-events.md) | Aware infrastructure hooks, `FactoryBean<T>` programmatic SPI, and `ApplicationEvent` dispatch. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/lifecycle/`](./src/main/java/com/spring/interview/lifecycle/):

```
03-bean-lifecycle-and-configuration/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/lifecycle/
    │   ├── processor/CustomValidationBeanPostProcessor.java    # Custom BPP tracking annotated beans
    │   ├── scopes/ScopedPrototypeManager.java                  # Safe dynamic Prototype resolution in Singleton
    │   ├── events/OrderEventPublisher.java                     # ApplicationEventPublisher & @EventListener demo
    │   └── factory/SecureClientFactoryBean.java                # FactoryBean SPI for complex client creation
    └── test/java/com/spring/interview/lifecycle/               # 100% Mocked Tier Test Suite (4 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
