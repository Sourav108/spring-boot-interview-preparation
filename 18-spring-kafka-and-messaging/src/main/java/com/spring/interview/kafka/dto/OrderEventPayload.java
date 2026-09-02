package com.spring.interview.kafka.dto;

import java.time.Instant;

/**
 * Immutable domain event payload transferred via Kafka topic.
 */
public record OrderEventPayload(
    String orderId,
    String customerEmail,
    double totalAmount,
    String status,
    Instant timestamp
) {
    public static OrderEventPayload of(String orderId, String customerEmail, double amount, String status) {
        return new OrderEventPayload(orderId, customerEmail, amount, status, Instant.now());
    }
}
