# 28-04: Live Debugging & Production Incident Scenarios (10 Case Studies)

> **Module**: `MOD-28: Interview Guide`
> **Topic ID**: `SB-28-04`
> **Target Role**: Senior / Staff / SDE2
> **Verification Date**: 2026-09-01

---

## 🛠️ Incident 1: High CPU Utilization (99%) & Thread Starvation
- **Symptom**: Application pod CPU jumps to 99%, HTTP response times exceed 30 seconds.
- **Diagnosis**: Run `jcmd <PID> Thread.print` or generate async-profiler flame graph.
- **Root Cause**: An unindexed regular expression (`Pattern.compile(".*(a|b)+.*")`) executed on user input in a loop, triggering Catastrophic Regex Backtracking.
- **Fix**: Pre-compile `java.util.regex.Pattern` as a static constant or rewrite with simple string matching.

---

## 🛠️ Incident 2: HikariCP Connection Pool Exhaustion (`ConnectionTimeoutException`)
- **Symptom**: Logs flood with `HikariPool-1 - Connection is not available, request timed out after 30000ms`.
- **Diagnosis**: Check `leak-detection-threshold=5000ms` stack traces.
- **Root Cause**: A developer invoked a slow third-party REST API call inside an active `@Transactional` method. The database connection was held idle for 20 seconds while waiting for the HTTP response.
- **Fix**: Move external REST / Kafka calls **OUTSIDE** the `@Transactional` boundary, holding JDBC connections strictly during SQL execution.

---

## 🛠️ Incident 3: Memory Leak Ending in `OutOfMemoryError: Java heap space`
- **Symptom**: Pod memory climbs monotonically until killed by Kubernetes Linux OOM.
- **Diagnosis**: Analyze HPROF heap dump in Eclipse MAT -> Dominator Tree.
- **Root Cause**: A static `ConcurrentHashMap<String, UserSession>` cache had no TTL eviction policy, accumulating 5 million expired sessions.
- **Fix**: Replace static Map with a Caffeine cache configured with `.expireAfterWrite(Duration.ofMinutes(30)).maximumSize(50000)`.

---

## 🛠️ Incident 4: Self-Invocation `@Transactional` Rollback Failure
- **Symptom**: Runtime exception is thrown, but partial database records remain committed.
- **Diagnosis**: Trace caller stack trace; method was invoked via `this.saveOrder()`.
- **Root Cause**: Calling a `@Transactional` method from within the same class bypasses the CGLIB AOP proxy.
- **Fix**: Extract method into a separate `@Service` bean or use `TransactionTemplate`.

---

## 🛠️ Incident 5: Kafka Consumer Partition Rebalance Loop
- **Symptom**: Kafka consumer repeatedly stops processing and triggers continuous group rebalances.
- **Diagnosis**: Check `max.poll.interval.ms` vs record processing duration.
- **Root Cause**: A batch of 500 records took 6 minutes to process, exceeding default `max.poll.interval.ms=300000` (5 minutes). Broker assumed consumer was dead.
- **Fix**: Decrease `max.poll.records` to 50 or increase `max.poll.interval.ms` to 10 minutes.

---

## 🛠️ Incident 6: Thread Pinning on Java 21 Virtual Threads
- **Symptom**: Enabling Virtual Threads fails to increase throughput; carrier thread CPU stays pegged.
- **Diagnosis**: Run with `-Djdk.tracePinnedThreads=full`.
- **Root Cause**: A third-party library used `synchronized` blocks around blocking socket I/O.
- **Fix**: Replace `synchronized` with `java.util.concurrent.locks.ReentrantLock`.

---

## 🛠️ Incident 7: Redis OOM Error (`OOM command not allowed`)
- **Symptom**: Redis rejects all write operations.
- **Diagnosis**: Run `redis-cli info memory` and `redis-cli --bigkeys`.
- **Root Cause**: `@Cacheable` entries were created without setting a default TTL in `RedisCacheConfiguration`.
- **Fix**: Configure `RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))` and set `maxmemory-policy allkeys-lru`.

---

## 🛠️ Incident 8: Flyway Migration Checksum Mismatch
- **Symptom**: Application fails to boot with `FlywayValidateException: Migration checksum mismatch for migration version 1.2`.
- **Diagnosis**: Inspect `flyway_schema_history` table checksum vs local SQL file.
- **Root Cause**: A developer edited an already-applied migration script instead of creating a new `V1_3__...` file.
- **Fix**: Revert modified file and create a new forward migration script, or run `flyway.repair()` in dev.

---

## 🛠️ Incident 9: Kubernetes Rolling Update Dropping In-Flight Traffic (502 Bad Gateway)
- **Symptom**: Deploying a new deployment version causes a 2-second spike of 502 errors.
- **Diagnosis**: Check Spring shutdown logs.
- **Root Cause**: Tomcat terminated immediately upon receiving `SIGTERM` without draining connections.
- **Fix**: Enable `server.shutdown: graceful` and `spring.lifecycle.timeout-per-shutdown-phase: 30s`.

---

## 🛠️ Incident 10: Spring Security Method Authorization Bypass
- **Symptom**: Unauthorized users successfully execute admin actions annotated with `@PreAuthorize`.
- **Diagnosis**: Check configuration classes.
- **Root Cause**: Missing `@EnableMethodSecurity` annotation on `@Configuration` class.
- **Fix**: Add `@EnableMethodSecurity` to security configuration.
