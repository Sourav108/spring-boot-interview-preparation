# 30-07: Spring Boot Testing Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-07`
> **Primary Technology**: JUnit 5 | Mockito 5.15 | Testcontainers 1.20 | Spring Boot 3.4
> **Verification Date**: 2026-09-01

---

## 🧪 Spring Boot Test Slice Matrix

| Test Slice | Target Scope | Key Injected Beans | Speed |
|---|---|---|:---:|
| **Pure Unit Test** | Single Class | Mockito `@Mock` / `@InjectMocks` | **< 10ms ⚡** |
| **`@WebMvcTest`** | Controller / Web Layer | `MockMvc`, Jackson, `@MockitoBean` | **< 200ms ⚡** |
| **`@DataJpaTest`** | Persistence Repositories | `TestEntityManager`, `DataSource` | **< 500ms** |
| **`@RestClientTest`** | HTTP Clients | `MockRestServiceServer` | **< 150ms** |
| **`@SpringBootTest`** | Full Integration Container | Full `ApplicationContext`, Testcontainers | **2–5 seconds** |
