package com.spring.interview.architecture.idempotency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class IdempotencyEngineTest {

    private final IdempotencyEngine engine = new IdempotencyEngine();

    @BeforeEach
    void setUp() {
        engine.clear();
    }

    @Test
    @DisplayName("Should successfully acquire lock on first request and reject concurrent collision")
    void shouldAcquireAndRejectCollision() {
        boolean first = engine.acquireExecutionLock("ik-100", Duration.ofSeconds(10));
        assertThat(first).isTrue();

        // Second concurrent request with same key must be rejected
        boolean second = engine.acquireExecutionLock("ik-100", Duration.ofSeconds(10));
        assertThat(second).isFalse();

        var record = engine.getRecord("ik-100");
        assertThat(record).isNotNull();
        assertThat(record.status()).isEqualTo(IdempotencyEngine.Status.PROCESSING);
    }

    @Test
    @DisplayName("Should transition from PROCESSING to COMPLETED and store response payload")
    void shouldCompleteAndStoreResponse() {
        engine.acquireExecutionLock("ik-200", Duration.ofSeconds(10));

        engine.recordCompletion("ik-200", "{\"transactionId\":\"tx_999\",\"status\":\"SUCCESS\"}", Duration.ofHours(24));

        var record = engine.getRecord("ik-200");
        assertThat(record).isNotNull();
        assertThat(record.status()).isEqualTo(IdempotencyEngine.Status.COMPLETED);
        assertThat(record.responsePayload()).contains("tx_999");
    }
}
