# 28-02: Senior Engineer Spring Boot Interview Playbook

> **Target Role**: Senior Software Engineer / Senior Backend Engineer
> **Key Focus Areas**: Spring Boot Auto-Configuration Internals, Security & OAuth2, Redis Caching Topologies, Kafka Event Pipelines, Resilience4j Fault Tolerance
> **Verification Date**: 2026-09-01

---

## 🎯 What Interviewers Look For at Senior Level
1. **Auto-Configuration Mechanics**: `@EnableAutoConfiguration`, `AutoConfigurationImportSelector`, `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`, and `@ConditionalOn...` evaluation order.
2. **Distributed Systems Resilience**: Implementing circuit breakers (`COUNT_BASED` sliding windows), rate limiters, bulkhead isolation, and fallback exception hierarchies.
3. **Event-Driven Messaging**: Kafka partition rebalancing, non-blocking retries (`@RetryableTopic`), Dead Letter Topics (`@DltHandler`), consumer deduplication idempotence.
4. **Security Architecture**: `SecurityFilterChain`, custom JWT claim converters, method security SpEL evaluation (`@PreAuthorize`), and OAuth 2.0 PKCE flow.

---

## 📋 The 10 Most Common Senior Interview Questions & Answers

### 1. How does Spring Boot Auto-Configuration work under the hood?
**Answer**: "`@SpringBootApplication` includes `@EnableAutoConfiguration`, which uses `AutoConfigurationImportSelector` to load configuration class candidates from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`. The engine filters candidates using `@ConditionalOnClass`, `@ConditionalOnMissingBean`, and `@ConditionalOnProperty` annotations via `OnClassCondition` byte-code inspection. Validated configuration beans are ordered via `@AutoConfigureBefore` / `@AutoConfigureAfter` and registered into the `BeanDefinitionRegistry` before user-defined beans are initialized."

### 2. How does `@RetryableTopic` achieve non-blocking retries in Apache Kafka?
**Answer**: "Standard in-place retries (`DefaultErrorHandler`) block the consumer thread at the head of the partition, stalling all subsequent healthy messages on that partition. Spring Kafka's `@RetryableTopic` solves this via non-blocking multi-topic routing: failed records are published to a dedicated delayed retry topic (e.g. `order-events-retry-1000`) and the offset on the main topic is committed immediately. Separate listener containers poll the retry topics with scheduled backoffs, and if all retry attempts fail, the record routes to the Dead Letter Topic (`@DltHandler`) without ever stalling the main stream."

### 3. How do you implement a distributed lock in Redis with zero race conditions?
**Answer**: "We acquire the lock using `SET lock_key unique_uuid NX PX 30000` (atomic Set if Not Exists with 30s TTL). To release the lock, we must never call plain `DEL` (which could delete a lock acquired by another thread if our execution exceeded TTL). Instead, we execute an atomic Lua script: `if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end`, ensuring only the thread holding the matching UUID can release the lock."

### 4. What is the difference between `@PreAuthorize` and `@Secured`?
**Answer**: "`@Secured` is a legacy Spring Security annotation supporting only simple role strings (`ROLE_ADMIN`). `@PreAuthorize` is part of modern `@EnableMethodSecurity` and supports rich Spring Expression Language (SpEL) expressions: evaluating method parameters (`@PreAuthorize('#userId == authentication.principal.id')`), checking authorities (`hasAuthority('SCOPE_read')`), and invoking custom security evaluation beans."

### 5. What are the 4 failure modes of caching in high-scale systems?
**Answer**: "1) **Cache Penetration**: Querying non-existent keys (mitigated by Bloom filters or caching null objects). 2) **Cache Avalanche**: Mass simultaneous TTL expirations (mitigated by adding random jitter to TTL). 3) **Cache Breakdown**: Expiration of a single ultra-hot key under heavy traffic (mitigated by mutex locks or `@Cacheable(sync=true)`). 4) **Cache Stampede**: Concurrent expensive recomputations on cache misses (mitigated by probabilistic early expiration)."
