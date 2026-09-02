# Module 17: Spring Cache & Redis

> **Module Code**: `MOD-17`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Cache | Redis & Lettuce | Distributed Locking | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise caching and distributed locking architectures in Spring Boot: `CacheManager` and `Cache` AOP proxy interception, SpEL key generators and filtering (`condition` vs `unless`), Lettuce non-blocking Netty connection multiplexing, Redis JSON serializers, per-cache TTL configuration, comparing caching topologies (Cache-Aside, Write-Through, Write-Behind, Refresh-Ahead), mitigating the 4 classic cache failure modes (Cache Stampede, Cache Penetration, Cache Avalanche, Cache Breakdown), and implementing production-grade distributed mutex locks in Redis via `SETNX PX` and atomic Lua release scripts.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-17-01** | [`01-spring-cache-abstraction-and-proxy-internals.md`](./01-spring-cache-abstraction-and-proxy-internals.md) | `CacheManager`, `CacheInterceptor`, SpEL key generation, `condition` vs `unless`, self-invocation bypass. |
| **SB-17-02** | [`02-redis-integration-lettuce-serializers-and-ttl.md`](./02-redis-integration-lettuce-serializers-and-ttl.md) | Lettuce vs Jedis, `RedisTemplate`, Jackson JSON serializers vs JDK binary blob risks, per-cache TTLs. |
| **SB-17-03** | [`03-caching-patterns-cache-aside-write-through-write-behind.md`](./03-caching-patterns-cache-aside-write-through-write-behind.md) | Cache-Aside, Write-Through, Write-Behind (Write-Back) trade-offs, and why you must evict on write. |
| **SB-17-04** | [`04-cache-stampede-penetration-avalanche-and-breakdown.md`](./04-cache-stampede-penetration-avalanche-and-breakdown.md) | Thundering Herd / Stampede, Bloom filters, Null Object caching, TTL jitter, and `@Cacheable(sync=true)`. |
| **SB-17-05** | [`05-distributed-locks-with-redis-lua-scripts-and-redisson.md`](./05-distributed-locks-with-redis-lua-scripts-and-redisson.md) | Redis distributed locks (`SETNX PX`), atomic Lua release scripts, Redlock algorithm, and Redisson Watchdog. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/cache/`](./src/main/java/com/spring/interview/cache/):

```
17-spring-cache-and-redis/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/cache/
    │   ├── lock/RedisAtomicDistributedLock.java                 # Redis distributed lock with Lua script release
    │   ├── service/ProductCatalogCacheService.java              # @Cacheable, @CachePut, @CacheEvict with metrics
    │   └── SpringCacheApplication.java                          # Executable application entrypoint
    └── test/
        ├── java/com/spring/interview/cache/                     # 100% Mocked Tier Test Suite (3 Unit Tests)
        └── resources/application.properties                     # In-memory Caffeine test configuration
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
