# 17-03: Caching Architectural Patterns: Cache-Aside vs Write-Through vs Write-Behind

> **Module**: `MOD-17: Spring Cache & Redis`
> **Topic ID**: `SB-17-03`
> **Prerequisites**: `SB-17-01`
> **Primary Technology**: Java 21 LTS | System Architecture | Caching Topologies
> **Verification Date**: 2026-09-01

---

## 1. Problem
When designing low-latency distributed systems, how should the application synchronize mutations between the cache layer (Redis) and the persistent database (PostgreSQL) while balancing consistency, latency, and write throughput?

---

## 2. The 4 Fundamental Caching Topologies

```mermaid
flowchart TD
    P{"Choose Caching Pattern"}

    P -->|1. Cache-Aside (Lazy-Loading) 🏆 Dominant| C1["Application reads from Cache; on miss reads DB & populates Cache. Writes update DB then evict Cache."]
    P -->|2. Write-Through| C2["Application writes to Cache; Cache synchronously writes to Database before returning."]
    P -->|3. Write-Behind (Write-Back) ⚡ Fastest Writes| C3["Application writes to Cache immediately; Cache asynchronously batches writes to Database in background."]
    P -->|4. Refresh-Ahead| C4["Cache automatically refreshes hot keys before TTL expires based on access patterns."]
```

---

## 3. Comprehensive Architectural Comparison Matrix

| Dimension | Cache-Aside (Lazy) | Write-Through | Write-Behind (Write-Back) |
|---|:---:|:---:|:---:|
| **Read Latency** | Low (on hit) / High (on miss) | Low | Low |
| **Write Latency** | Moderate (DB write + cache evict) | High (Synchronous 2-phase write) | **Ultra-Low (In-memory write only) ⚡** |
| **Data Consistency** | Eventual Consistency | Strong Consistency | Eventual Consistency |
| **Data Loss Risk on Crash** | Zero | Zero | **High (If Redis crashes before DB sync)** |
| **Complexity** | Simple | Moderate | Complex (Queue buffer + worker thread) |
| **Best For** | General Web / Microservices | Financial systems, balance checks | High-volume IoT telemetry, gaming analytics |

---

## 4. Cache-Aside Cache Invalidation: Update Cache vs Evict Cache
Should you update the cache on write (`cache.put()`) or evict it (`cache.evict()`)?
> [!IMPORTANT]
> **Always Evict (`cache.evict()`)!**
> If Thread 1 and Thread 2 concurrently write to the DB, race conditions can cause Thread 1 to overwrite Thread 2's newer cache entry, leaving stale data in cache indefinitely. Evicting the key forces the next reader to load authoritative DB data cleanly.

---

## 5. Common Mistakes
- **Updating cache directly instead of evicting**: Leads to phantom stale cache entries during concurrent writes.

---

## 6. Interview Questions
1. **SDE2**: Walk me through the Cache-Aside read and write lifecycle.
2. **Senior**: Why is Write-Behind caching rarely used for critical transactional systems like financial ledgers?

---

## 7. Interview Answer (Senior Level)
"In Cache-Aside, the application first reads from cache; on a miss, it reads from the DB, populates the cache, and returns. On writes, the application writes to the DB and evicts the corresponding cache key (`@CacheEvict`). Write-Behind caching writes solely to the in-memory cache and acknowledges success immediately, queueing database updates in background batches. While Write-Behind achieves ultra-high write throughput, it is unsuitable for financial ledgers because if the cache node crashes before the dirty write buffer is flushed to disk, uncommitted transactions are permanently lost, violating ACID durability."
