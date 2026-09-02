# Project 08: High-Throughput IoT Telemetry Ingestion Pipeline (1M Events/min)

> **Project Code**: `PRJ-08`
> **Level**: Senior / Staff
> **Primary Technology**: Java 21 LTS | Apache Kafka 3.9 | Spring Kafka Batching | TimescaleDB / PostgreSQL

---

## 🏗️ Architecture & Domain Model
A real-time time-series telemetry ingestion engine ingesting 1,000,000 sensor telemetry metrics/minute from industrial smart meters, aggregating sliding-window averages, and writing batches to TimescaleDB.

```mermaid
flowchart LR
    IoT["10,000 IoT Smart Meters"] -->|HTTP / MQTT / TCP| Gateway["Ingestion Gateway"]
    Gateway -->|KafkaTemplate Async Batch| KafkaTopic[("Kafka 'telemetry-stream' (16 Partitions)")]

    KafkaTopic -->|Batch Consumer (500 records/poll)| Workers["Spring Kafka Batch Listeners (16 Threads)"]
    Workers -->|COPY / Batch INSERT| Timescale[("TimescaleDB Hypertable (Time-Series Data) ⚡")]
```

---

## 🔑 Key Engineering Highlights
1. **High-Throughput Producer Tuning**: `linger.ms=20`, `batch.size=65536`, `compression.type=lz4` packing thousands of telemetry points into single network packets.
2. **PostgreSQL Batch Inserts**: `JdbcTemplate.batchUpdate()` / PostgreSQL `COPY` reducing DB roundtrips from 1,000,000 queries to 2,000 bulk batch flushes per minute.

---

## 💬 Interview Talking Points
- *Question*: "How do you achieve 1 million inserts per minute into PostgreSQL without exhausting connection pools?"
- *Answer*: "We ingest events asynchronously into a partitioned Kafka topic and consume in micro-batches of 500 records per poll using `@KafkaListener(batch = \"true\")`. Instead of executing individual `INSERT` statements, we execute parameterized batch updates (`batchUpdate`) or raw PostgreSQL binary `COPY` streams over a small fixed pool of 15 connections, turning millions of chatty network roundtrips into compact sequential disk writes."
