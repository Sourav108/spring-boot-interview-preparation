# Module 23: Performance & Tuning

> **Module Code**: `MOD-23`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Generational ZGC | HikariCP Physics | Virtual Threads | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master systematic performance engineering and bottleneck diagnosis in Spring Boot: JVM tuning flags and Garbage Collection algorithms (G1 GC vs Java 21 Generational ZGC sub-millisecond pauses), PostgreSQL and HikariCP connection pool sizing mathematics ($T_N = C \times 2 + I$), Tomcat platform thread tuning vs Java 21 Virtual Threads (`Loom`), thread pinning diagnostics, production profiling using Java Flight Recorder (JFR), async-profiler flame graphs, Eclipse Memory Analyzer (MAT) leak investigations, and cold-start optimization via Application Class Data Sharing (AppCDS) and GraalVM Native Images.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-23-01** | [`01-jvm-tuning-heap-sizing-and-gc-selection-g1-vs-zgc.md`](./01-jvm-tuning-heap-sizing-and-gc-selection-g1-vs-zgc.md) | -Xms/-Xmx equality, Generational ZGC colored pointers/load barriers, and Metaspace sizing. |
| **SB-23-02** | [`02-hikaricp-connection-pool-sizing-formula-and-leak-detection.md`](./02-hikaricp-connection-pool-sizing-formula-and-leak-detection.md) | Proven PostgreSQL pool formula, CPU context-switch contention, and leak detection thresholds. |
| **SB-23-03** | [`03-tomcat-web-server-thread-tuning-vs-virtual-threads.md`](./03-tomcat-web-server-thread-tuning-vs-virtual-threads.md) | Tomcat thread pool limits vs Java 21 Virtual Threads, and diagnosing thread pinning traps. |
| **SB-23-04** | [`04-profiling-spring-boot-jfr-async-profiler-and-heap-dumps.md`](./04-profiling-spring-boot-jfr-async-profiler-and-heap-dumps.md) | JFR continuous recording, async-profiler CPU flame graphs, and Eclipse MAT dominator trees. |
| **SB-23-05** | [`05-startup-optimization-cds-aot-and-graalvm-native-images.md`](./05-startup-optimization-cds-aot-and-graalvm-native-images.md) | AppCDS class archives, Spring AOT build-time processing, and GraalVM Native Images vs JIT. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/performance/`](./src/main/java/com/spring/interview/performance/):

```
23-performance-and-tuning/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/performance/
    │   ├── calc/PoolSizingCalculator.java                       # HikariCP pool calculator based on DB hardware specs
    │   └── SpringPerformanceApplication.java                    # Executable application entrypoint
    └── test/java/com/spring/interview/performance/              # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
