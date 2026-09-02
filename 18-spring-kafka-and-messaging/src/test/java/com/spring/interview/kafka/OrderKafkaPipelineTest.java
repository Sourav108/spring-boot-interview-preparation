package com.spring.interview.kafka;

import com.spring.interview.kafka.consumer.OrderEventConsumer;
import com.spring.interview.kafka.dto.OrderEventPayload;
import com.spring.interview.kafka.producer.OrderEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 2, topics = {"order-events"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class OrderKafkaPipelineTest {

    @Autowired
    private OrderEventProducer producer;

    @Autowired
    private OrderEventConsumer consumer;

    @Test
    @DisplayName("Should publish order event and consume it asynchronously via @KafkaListener")
    void shouldPublishAndConsumeOrderEvent() throws Exception {
        var payload = OrderEventPayload.of("ORD-9001", "alice@enterprise.com", 250.0, "CREATED");

        producer.sendOrderEvent("order-events", payload).get(5, TimeUnit.SECONDS);

        // Wait up to 5 seconds for async listener processing
        long deadline = System.currentTimeMillis() + 5000;
        while (consumer.getProcessedEvents().isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(100);
        }

        assertThat(consumer.getProcessedEvents()).isNotEmpty();
        var received = consumer.getProcessedEvents().getFirst();
        assertThat(received.orderId()).isEqualTo("ORD-9001");
        assertThat(received.customerEmail()).isEqualTo("alice@enterprise.com");
    }

    @Test
    @DisplayName("Should route failed poison pill message to Dead Letter Topic via @RetryableTopic")
    void shouldRoutePoisonPillToDeadLetterTopic() throws Exception {
        var poisonPayload = OrderEventPayload.of("ORD-POISON-1", "bad@actor.com", 0.0, "POISON_PILL");

        producer.sendOrderEvent("order-events", poisonPayload).get(5, TimeUnit.SECONDS);

        // Wait for retries to exhaust and DLT handler to be invoked
        long deadline = System.currentTimeMillis() + 8000;
        while (consumer.getDeadLetterEvents().isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(150);
        }

        assertThat(consumer.getDeadLetterEvents()).isNotEmpty();
        var dltPayload = consumer.getDeadLetterEvents().getFirst();
        assertThat(dltPayload.orderId()).isEqualTo("ORD-POISON-1");
    }
}
