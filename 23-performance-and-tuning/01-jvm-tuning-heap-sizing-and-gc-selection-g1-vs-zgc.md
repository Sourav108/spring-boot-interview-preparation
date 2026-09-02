# 23-01: JVM Tuning for Spring Boot: Sizing Heaps & GC Selection (G1 vs ZGC)

> **Module**: `MOD-23: Performance & Tuning`
> **Topic ID**: `SB-23-01`
> **Prerequisites**: JVM Core Memory Architecture
> **Primary Technology**: Java 21 LTS | Generational ZGC | G1 GC Tuning
> **Verification Date**: 2026-09-01

---

## 1. Problem
Under high concurrency, poorly tuned JVM garbage collectors cause 200ms+ "Stop-the-World" (STW) pauses, leading to sudden HTTP client timeout spikes, failed health probes, and Kubernetes container evictions.

---

## 2. Why It Exists: GC Algorithms Compared (Java 21)

```mermaid
flowchart TD
    GC{"Choose Garbage Collector"}

    GC -->|1. G1 GC (Default)| G1["Throughput-oriented with bounded pauses (e.g. 50–100ms). Divides heap into 2048 regions. Best for balanced throughput."]
    GC -->|2. Generational ZGC 🏆 Java 21 Low Latency| Z1["Ultra-low pause times (< 1ms STW) regardless of heap size (1GB to 16TB!). Uses colored pointers & load barriers."]
    GC -->|3. Shenandoah GC| S1["Concurrent compaction with low pause times; alternative to ZGC."]
    GC -->|4. Parallel GC| P1["Maximizes CPU batch compute throughput with long pauses. Ideal for offline MapReduce/Spark."]
```

---

## 3. In-Depth Comparison Matrix for Spring Boot Services

| Dimension | G1 GC | Generational ZGC (Java 21) |
|---|:---:|:---:|
| **Pause Time (STW)** | ~50–200ms | **< 1ms (Sub-millisecond guaranteed) ⚡** |
| **Throughput Overhead** | ~2–5% | ~5–8% (Read barrier overhead) |
| **Max Heap Sizing** | 4GB–32GB | 4GB–16TB |
| **Flag to Enable** | `-XX:+UseG1GC` (Default) | `-XX:+UseZGC -XX:+ZGenerational` |
| **Best For** | High-throughput batch / standard APIs | Ultra-low latency SLA (FinTech / Trading / Realtime) |

---

## 4. Production JVM Startup Flags Template for 4GB Container
```bash
java \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -Xms3g -Xmx3g \
  -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m \
  -Xss512k \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/dumps/heapdump.hprof \
  -jar app.jar
```

---

## 5. Common Mistakes
- **Setting `-Xms` lower than `-Xmx` in Kubernetes pods**: Causes the JVM to dynamically allocate heap memory during runtime traffic spikes, triggering Linux OOM killer termination before reaching max capacity.

---

## 6. Interview Questions
1. **SDE2**: Why should `-Xms` and `-Xmx` be set to identical values in production containerized environments?
2. **Senior**: How does Generational ZGC achieve sub-millisecond pause times in Java 21?

---

## 7. Interview Answer (Senior Level)
"Setting `-Xms` equal to `-Xmx` pre-allocates the full JVM heap at boot time, avoiding OS memory re-allocation pauses during traffic surges and preventing Kubernetes from evicting containers when heap growth encounters cgroup memory limits. In Java 21, Generational ZGC separates memory into young and old generations and performs all phases (marking, relocation, and reference updates) concurrently with application threads using **Colored Pointers** and **Load Barriers**. Its Stop-the-World pauses are limited to trivial thread-local handshakes taking under 1 millisecond, even on multi-terabyte heaps, eliminating GC-induced tail latency spikes."
