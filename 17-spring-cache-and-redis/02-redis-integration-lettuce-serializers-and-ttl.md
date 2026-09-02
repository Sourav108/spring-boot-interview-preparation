# 17-02: Spring Data Redis: Lettuce, Serializers & Per-Cache TTL

> **Module**: `MOD-17: Spring Cache & Redis`
> **Topic ID**: `SB-17-02`
> **Prerequisites**: `SB-17-01`
> **Primary Technology**: Java 21 LTS | Redis | Lettuce Client & Serialization
> **Verification Date**: 2026-09-01

---

## 1. Problem
By default, `RedisTemplate` uses standard Java serialization (`JdkSerializationRedisSerializer`). This produces unreadable binary blobs (`\xac\xed\x00\x05...`), causes `ClassNotFoundException` / `InvalidClassException` across service deployments, and poses dangerous remote code execution (RCE) deserialization vulnerabilities.

---

## 2. Why It Exists: Lettuce vs Jedis
- **Lettuce** *(Default in Spring Boot 3)*: Built on **Netty non-blocking I/O**. A single shared thread-safe connection serves thousands of concurrent requests asynchronously.
- **Jedis**: Blocking socket I/O. Requires a heavy `GenericObjectPool` connection pool where each concurrent thread holds a separate physical socket.

---

## 3. Architecture: Redis Serializers Comparison

```mermaid
flowchart TD
    JavaObj["Java Object: UserRecord('alice', 30)"] --> Serializer{"Choose Serializer"}

    Serializer -->|1. JdkSerializationRedisSerializer ⚠️| Bin["Binary Blob: \\xac\\xed... (Fragile, Incompatible, Security Vulnerability)"]
    Serializer -->|2. StringRedisSerializer| Str["Plain UTF-8 String (For Keys & IDs)"]
    Serializer -->|3. GenericJackson2JsonRedisSerializer 🏆| JSON["Readable JSON with Type Info: {\"@class\": \"...\", \"username\": \"alice\"}"]
```

---

## 4. Production RedisCacheManager Configuration with Custom TTLs
```java
@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(10)) // Default TTL
        .disableCachingNullValues()
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

    Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
        "products", defaultConfig.entryTtl(Duration.ofHours(1)),
        "exchange_rates", defaultConfig.entryTtl(Duration.ofMinutes(1)),
        "user_sessions", defaultConfig.entryTtl(Duration.ofDays(7))
    );

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
}
```

---

## 5. Common Mistakes
- **Omitting TTL on Redis cache entries**: Redis memory fills up until `OOM command not allowed when used memory > 'maxmemory'` crashes the application.

---

## 6. Interview Questions
1. **SDE2**: Why is Lettuce preferred over Jedis as the default Redis client in Spring Boot?
2. **Senior**: Why is `JdkSerializationRedisSerializer` dangerous in production microservices, and how do you configure Jackson JSON serialization safely?

---

## 7. Interview Answer (Senior Level)
"Lettuce is built on Netty's asynchronous non-blocking event loops, allowing a single thread-safe connection to multiplex requests from thousands of threads, whereas Jedis uses blocking I/O requiring dedicated connection pooling. `JdkSerializationRedisSerializer` is an anti-pattern: it bloats payload size, breaks whenever `serialVersionUID` or class structures change across deployments, and is vulnerable to Java deserialization RCE exploits. We configure `GenericJackson2JsonRedisSerializer` or `Jackson2JsonRedisSerializer`, which outputs human-readable JSON payloads, reduces network bandwidth, enables cross-language polyglot access (Python/Go services can read the cache), and ensures backward-compatible deserialization."
