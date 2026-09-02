# 30-05: Spring Kafka & Messaging Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-05`
> **Primary Technology**: Spring for Apache Kafka 3.3.2 | Kafka 3.9
> **Verification Date**: 2026-09-01

---

## ⚡ Non-Blocking `@RetryableTopic` Configuration
```java
@RetryableTopic(
    attempts = "4",
    backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
    autoCreateTopics = "true",
    dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(topics = "order-events", groupId = "order-fulfillment-group")
public void handleOrderEvent(OrderEvent event) {
    // Process event asynchronously with non-blocking retry routing
}

@DltHandler
public void handleDlt(OrderEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    // Dead Letter Topic sink handler for permanent failures
}
```
