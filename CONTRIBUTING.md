# Contributing to Spring Boot Interview Preparation

Thank you for contributing to the **Spring Boot Interview Preparation** curriculum! This repository is designed to be the definitive, implementation-first resource for senior backend engineers mastering Spring Boot and production systems engineering.

---

## 📜 Core Guiding Principles

1. **Original Educational Content**: All lessons, code examples, architectural diagrams, and interview solutions must be 100% original.
2. **Java 21 LTS & Spring Boot 3.4+ Baseline**: All code must utilize modern Java 21 features and pinned GA dependencies from [`VERSION_MATRIX.md`](./VERSION_MATRIX.md).
3. **Evidence-Driven Engineering**: Never teach annotations without explaining their runtime bytecode, proxy, reflection, or lifecycle mechanics.
4. **Runnable & Testable**: Every project and code sample must compile cleanly under Maven (`mvn clean test`) and pass unit/Testcontainers integration tests.

---

## 🛠️ Contribution Workflow

1. **Fork and Clone** the repository.
2. **Create a Feature Branch**:
   ```bash
   git checkout -b feat/add-module-topic
   ```
3. **Follow the Standard Lesson Template**: Structure every substantial technical lesson using [`templates/lesson-template.md`](./templates/lesson-template.md).
4. **Adhere to the Code Quality Gate**:
   - Write clean, idiomatic Java 21 code (Records, Pattern Matching, Sealed Types).
   - Ensure all unit tests run in the zero-cost mocked tier without hardcoded secrets.
   - For database integration tests, use real PostgreSQL instances via Testcontainers.
5. **Verify Hygiene**:
   ```bash
   # Verify no trailing whitespace or format issues
   git diff --check
   ```
6. **Submit a Pull Request** with a detailed summary of changes.

---

## 📐 Lesson Structure Standards

Every lesson must provide:
- **Understand**: Clear explanation of the fundamental problem and why Spring's solution exists.
- **Visualize**: Mermaid diagrams illustrating lifecycles, proxy chains, or request paths.
- **Implement**: Minimal and production-grade code examples.
- **Debug & Failure Modes**: Root cause analysis of common production failures.
- **Interview & Defense**: SDE2/Senior interview questions, answers, and architectural trade-off evaluations.
