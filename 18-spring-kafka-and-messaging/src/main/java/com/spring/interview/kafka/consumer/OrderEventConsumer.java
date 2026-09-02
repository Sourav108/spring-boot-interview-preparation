package com.spring.interview.kafka.consumer;

import com.spring.interview.kafka.dto.OrderEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Consumer demonstrating @RetryableTopic non-blocking retries and Dead Letter Topic handling.
 */
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final List<OrderEventPayload> processedEvents = new ArrayList<>();
    private final List<OrderEventPayload> deadLetterEvents = new ArrayList<>();

    @RetryableTopic(
        attempts = "2",
        backoff = @Backoff(delay = 100, multiplier = 1.5),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void consumeOrderEvent(@Payload OrderEventPayload event) {
        log.info("Processing order event: {}", event.orderId());
        if ("POISON_PILL".equals(event.status())) {
            throw new RuntimeException("Simulated poison pill failure for order: " + event.orderId());
        }
        processedEvents.add(event);
    }

    @DltHandler
    public void handleDeadLetterRecord(
        @Payload OrderEventPayload payload,
        @Header(KafkaHeaders.ORIGINAL_TOPIC) String originalTopic
    ) {
        log.error("Dead Letter Received: Order {} from original topic {}", payload.orderId(), originalTopic);
        deadLetterEvents.add(payload);
    }

    public List<OrderEventPayload> getProcessedEvents() {
        return Collections.unmodifiableList(processedEvents);
    }

    public List<OrderEventPayload> getDeadLetterEvents() {
        return Collections.unmodifiableList(deadLetterEvents);
    }
}
