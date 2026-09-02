# 17-04: Cache Failure Modes: Stampede, Penetration, Avalanche & Breakdown

> **Module**: `MOD-17: Spring Cache & Redis`
> **Topic ID**: `SB-17-04`
> **Prerequisites**: `SB-17-01`, `SB-17-02`
> **Primary Technology**: Java 21 LTS | Distributed Systems Resilience | Cache Failure Modes
> **Verification Date**: 2026-09-01

---

## 1. Problem
Under heavy production load (100,000 requests/sec), subtle cache anomalies can instantly overwhelm the relational database, causing cascading connection pool exhaustion and total system outages.

---

## 2. The 4 Classic Cache Failure Modes & Architectures

```mermaid
flowchart TD
    Modes{"The 4 Cache Failure Modes"}

    Modes -->|1. Cache Penetration| M1["Querying non-existent keys (id=-9999). Every request misses cache and hits DB! 🛑<br><b>Fix:</b> Bloom Filters or Caching Null Objects with short TTL."]

    Modes -->|2. Cache Avalanche| M2["Thousands of cached keys share the exact same TTL and expire simultaneously! DB suffers massive traffic spike. 🛑<br><b>Fix:</b> Add random jitter to TTL (e.g. TTL = 300s + Random(0..60s))."]

    Modes -->|3. Cache Breakdown (Hotspot Invalid)| M3["A single ultra-hot key (e.g. iPhone flash sale) expires while under 50k req/s. All threads hit DB at once! 🛑<br><b>Fix:</b> Distributed Mutex / Lock or @Cacheable(sync=true)."]

    Modes -->|4. Cache Stampede (Thundering Herd)| M4["Parallel workers concurrently execute expensive recalculations on cache miss. 🛑<br><b>Fix:</b> Probabilistic early expiration (XFetch algorithm)."]
```

---

## 3. Deep-Dive Fix Strategies in Spring Boot

### Strategy 1: Preventing Cache Breakdown with `@Cacheable(sync = true)`
Spring Cache supports synchronized cache resolution:
```java
@Cacheable(value = "hot_products", key = "#id", sync = true)
public Product getHotProduct(Long id) {
    // Only ONE thread executes the DB query on cache miss! Other threads wait for cache population.
    return productRepository.findById(id).orElseThrow();
}
```

### Strategy 2: Preventing Cache Avalanche via Random Jitter
```java
public Duration calculateJitteredTtl(Duration baseTtl) {
    long jitterSeconds = ThreadLocalRandom.current().nextLong(0, 60);
    return baseTtl.plusSeconds(jitterSeconds);
}
```

### Strategy 3: Preventing Cache Penetration via Null Caching / Bloom Filter
Store a dummy null marker in cache with a short TTL (1–2 minutes) so repeated queries for non-existent IDs hit the cache instead of the database.

---

## 4. Common Mistakes
- **Setting identical uniform TTL across all cache keys**: Guarantees a cache avalanche event at exactly TTL seconds after startup.

---

## 5. Interview Questions
1. **SDE2**: What is the difference between Cache Penetration and Cache Breakdown?
2. **Senior**: How does `@Cacheable(sync = true)` mitigate the Thundering Herd / Cache Breakdown problem in a single JVM?

---

## 6. Interview Answer (Senior Level)
"**Cache Penetration** occurs when queries for non-existent data (e.g. malicious IDs) bypass the cache and repeatedly pound the database; it is mitigated by Bloom filters or caching null objects with short TTLs. **Cache Breakdown** occurs when a single extremely hot key expires, causing thousands of concurrent requests to execute the same database query simultaneously. `@Cacheable(sync = true)` resolves Cache Breakdown inside a JVM by synchronizing the cache loader: the first thread to notice the miss acquires a lock and executes the DB query, while all other concurrent caller threads block and wait to consume the freshly populated cache entry, preventing database overload."
