# Module 18: Spring Kafka & Messaging

> **Module Code**: `MOD-18`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Apache Kafka 3.9 | Spring Kafka 3.3.2 | Non-Blocking DLT | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise event-driven architectures with Spring for Apache Kafka: storage log append architecture (topics, partitions, segment files), consumer group partition rebalancing, offset commit strategies (`BATCH`, `RECORD`, `MANUAL`), asynchronous `KafkaTemplate` sending with `CompletableFuture`, multi-threaded `@KafkaListener` concurrency, resilient non-blocking error handling via `@RetryableTopic` and Dead Letter Topics (`@DltHandler`), consumer deduplication idempotence, transactional messaging (Kafka Transactions + DB Exactly-Once Semantics), and the Transactional Outbox Pattern with Debezium CDC and distributed Saga orchestration.

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-18-01** | [`01-kafka-architecture-partitions-offsets-and-consumer-groups.md`](./01-kafka-architecture-partitions-offsets-and-consumer-groups.md) | Kafka storage model, log append segments, partition rebalancing, and consumer offset commit strategies. |
| **SB-18-02** | [`02-spring-kafka-producers-and-consumers.md`](./02-spring-kafka-producers-and-consumers.md) | `KafkaTemplate` async callbacks, `@KafkaListener`, listener container concurrency, and zero-loss configs. |
| **SB-18-03** | [`03-error-handling-retryabletopic-and-dead-letter-topics.md`](./03-error-handling-retryabletopic-and-dead-letter-topics.md) | Blocking vs non-blocking retries, multi-tier retry topic routing, and Dead Letter Topics (`@DltHandler`). |
| **SB-18-04** | [`04-idempotent-consumers-and-transactional-messaging.md`](./04-idempotent-consumers-and-transactional-messaging.md) | Producer idempotence (`enable.idempotence`), consumer deduplication store, and Kafka Exactly-Once Semantics (EOS). |
| **SB-18-05** | [`05-outbox-pattern-and-event-driven-sagas.md`](./05-outbox-pattern-and-event-driven-sagas.md) | Dual-write dilemma, Transactional Outbox pattern with Debezium CDC, and Orchestrated vs Choreographed Sagas. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/kafka/`](./src/main/java/com/spring/interview/kafka/):

```
18-spring-kafka-and-messaging/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/kafka/
    │   ├── dto/OrderEventPayload.java                           # Immutable Java 21 Record for Kafka messages
    │   ├── producer/OrderEventProducer.java                     # Asynchronous KafkaTemplate publisher
    │   ├── consumer/OrderEventConsumer.java                     # @KafkaListener with @RetryableTopic & @DltHandler
    │   └── SpringKafkaApplication.java                          # Executable application entrypoint with Topic bean
    └── test/
        ├── java/com/spring/interview/kafka/                     # 100% Mocked Tier Test Suite (@EmbeddedKafka Integration Tests)
        └── resources/application.properties                     # JSON serializer / deserializer configuration
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
