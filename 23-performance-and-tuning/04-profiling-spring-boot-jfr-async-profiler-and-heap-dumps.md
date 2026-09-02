# 23-04: Profiling Spring Boot: JFR, async-profiler Flame Graphs & Eclipse MAT

> **Module**: `MOD-23: Performance & Tuning`
> **Topic ID**: `SB-23-04`
> **Prerequisites**: `SB-23-01`
> **Primary Technology**: Java 21 LTS | Java Flight Recorder | Profiling Flame Graphs
> **Verification Date**: 2026-09-01

---

## 1. Problem
When a service in production exhibits 95% CPU utilization or progressive memory consumption ending in `OutOfMemoryError: Java heap space`, how do you diagnose the root-cause method or memory leak without adding heavy debugging code?

---

## 2. The 3 Essential JVM Diagnostics Tools

```mermaid
flowchart TD
    Diag{"Profiling & Diagnostics"}

    Diag -->|1. Java Flight Recorder (JFR)| J["Built directly into OpenJDK. Captures lock contention, memory allocations, and I/O latency with < 1% overhead! 🛡️"]
    Diag -->|2. async-profiler| A["Low-overhead sampling CPU and memory profiler. Generates interactive Flame Graphs! 🔥"]
    Diag -->|3. Eclipse Memory Analyzer (MAT)| M["Analyzes HPROF heap dumps to find Dominator Trees and memory leak suspects. 🔍"]
```

---

## 3. How to Generate Continuous JFR Recordings in Production
```bash
# Start continuous circular JFR recording keeping the last 60 minutes:
java -XX:StartFlightRecording=disk=true,dumponexit=true,filename=/dumps/recording.jfr,maxage=1h -jar app.jar
```

---

## 4. Reading Flame Graphs: Understanding CPU Bottlenecks

```
[========================== org.apache.catalina.core.StandardEngineValve.invoke() ==========================]
  [==================== com.spring.interview.controller.OrderController.checkout() ====================]
    [======== Jackson JSON Serialization (15%) ========] [======== Regex / Cryptography (70%) 🚨 ========]
```
- **X-Axis**: Alphabetical aggregation of stack frames (width = % of CPU time spent in that function).
- **Y-Axis**: Call stack depth.
- **Root Cause**: The widest plateaus at the top of the flame graph are where CPU cycles are consumed.

---

## 5. Common Mistakes
- **Using older intrusive bytecode instrumentation profilers in production**: Adds 20–50% overhead and skews latency measurements; always use sampling-based profilers (JFR or async-profiler).

---

## 6. Interview Questions
1. **SDE2**: What is Java Flight Recorder (JFR) and what is its production overhead?
2. **Senior**: Walk me through how you locate and resolve a memory leak using an HPROF heap dump in Eclipse MAT.

---

## 7. Interview Answer (Senior Level)
"Java Flight Recorder (JFR) is an event-tracing diagnostic engine built into the HotSpot JVM kernel that records CPU execution, memory allocations, thread contention, and I/O latency with under 1% overhead, making it safe for production continuous recording. When diagnosing an `OutOfMemoryError`, we capture an HPROF heap dump (`-XX:+HeapDumpOnOutOfMemoryError`) and open it in Eclipse MAT. We inspect the **Dominator Tree** and **Leak Suspects Report** to identify objects retaining the largest shallow vs retained heap sizes. We then follow the **Incoming References (excluding weak/soft references)** to trace the path to the GC Root (e.g. an unbounded static `ConcurrentHashMap` cache or an unclosed `ThreadLocal`), allowing us to patch the leak immediately."
