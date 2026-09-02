# 23-05: Startup Optimization: CDS, Spring AOT & GraalVM Native Images

> **Module**: `MOD-23: Performance & Tuning`
> **Topic ID**: `SB-23-05`
> **Prerequisites**: `SB-05-01`, `SB-23-01`
> **Primary Technology**: Java 21 LTS | GraalVM Native Image | Spring AOT & AppCDS
> **Verification Date**: 2026-09-01

---

## 1. Problem
In serverless functions (AWS Lambda) or auto-scaling Kubernetes deployments, standard Spring Boot cold start times (3–6 seconds) delay request processing and increase latency during traffic spikes.

---

## 2. The 3 Modern Startup Optimization Strategies Compared

```mermaid
flowchart TD
    Strategies{"Startup Optimization Strategies"}

    Strategies -->|1. Class Data Sharing (AppCDS)| C1["Dumps JVM parsed class metadata into a shared archive (.jsa). Skips class loading/verification at boot. <b>~30-50% faster boot, zero code changes!</b>"]

    Strategies -->|2. Spring AOT (Ahead-of-Time)| C2["Generates bytecode at build-time for bean definitions and reflection hints, skipping runtime classpath reflection scanning."]

    Strategies -->|3. GraalVM Native Image 🏆 Instant Boot| C3["Compiles Spring Boot directly into a standalone native OS binary (ELF executable). <b>~50ms startup time, 50MB RAM!</b>"]
```

---

## 3. Detailed Architectural Comparison

| Dimension | Standard JVM (JIT) | AppCDS on JVM | GraalVM Native Image (AOT) |
|---|:---:|:---:|:---:|
| **Cold Startup Time** | 3.5 – 6.0 seconds | 1.8 – 3.0 seconds | **30 – 80 milliseconds ⚡** |
| **Memory Footprint (RSS)** | 350MB – 600MB | 250MB – 400MB | **40MB – 80MB (Tiny!)** |
| **Peak Throughput (P99)** | **Highest (JIT C2 Optimizations)** | **Highest (JIT C2 Optimizations)** | Slightly Lower (No dynamic runtime profiling) |
| **Build Time** | 10 – 20 seconds | 20 – 30 seconds | **3 – 8 minutes (High build cost)** |
| **Dynamic Features** | Full dynamic reflection/CGLIB | Full dynamic reflection/CGLIB | Closed-world assumptions (Requires reflection hints) |

---

## 4. Enabling Application Class Data Sharing (AppCDS) in Spring Boot 3.3+
Spring Boot supports automatic CDS training run:
```bash
# 1. Perform CDS training run to generate archive
java -XX:ArchiveClassesAtExit=app.jsa -Dspring.context.exit=onRefresh -jar app.jar

# 2. Start container using the cached CDS archive (instant 40% speedup!)
java -XX:SharedArchiveFile=app.jsa -jar app.jar
```

---

## 5. Common Mistakes
- **Assuming GraalVM Native Images always achieve higher peak throughput than JIT**: Standard HotSpot JIT (C2 compiler) uses live runtime profile-guided compilation and inlining, often outperforming static AOT compilation for long-running high-throughput workloads.

---

## 6. Interview Questions
1. **SDE2**: What is Class Data Sharing (CDS) and how does it speed up Spring Boot boot time?
2. **Senior**: When should you compile Spring Boot to GraalVM Native Image versus running on HotSpot JVM with AppCDS?

---

## 7. Interview Answer (Senior Level)
"Class Data Sharing (CDS) pre-processes and loads application and framework classes into a memory-mapped archive file (`.jsa`), allowing the JVM to memory-map metadata directly into memory at boot time and skipping expensive bytecode parsing and verification for an instant 30–50% startup speedup with zero code changes. We choose **GraalVM Native Images** for scale-to-zero serverless environments (AWS Lambda, Knative) where sub-100ms startup times and minimal memory footprint (50MB RSS) are mandatory. For long-running, steady-state enterprise microservices where peak sustained throughput and aggressive JIT optimization outweigh cold-start latency, we run standard HotSpot JVM with Generational ZGC and AppCDS."
