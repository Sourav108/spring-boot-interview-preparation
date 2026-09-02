package com.spring.interview.architecture.idempotency;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Idempotency manager tracking PROCESSING and COMPLETED states with TTL expiration.
 */
@Component
public class IdempotencyEngine {

    public enum Status { PROCESSING, COMPLETED }

    public record IdempotencyRecord(Status status, String responsePayload, long expiresAtEpoch) {}

    private final Map<String, IdempotencyRecord> storage = new ConcurrentHashMap<>();

    public synchronized boolean acquireExecutionLock(String key, Duration ttl) {
        cleanExpired();
        IdempotencyRecord existing = storage.get(key);
        if (existing != null) {
            return false;
        }
        storage.put(key, new IdempotencyRecord(Status.PROCESSING, null, System.currentTimeMillis() + ttl.toMillis()));
        return true;
    }

    public synchronized void recordCompletion(String key, String responsePayload, Duration retention) {
        storage.put(key, new IdempotencyRecord(Status.COMPLETED, responsePayload, System.currentTimeMillis() + retention.toMillis()));
    }

    public synchronized IdempotencyRecord getRecord(String key) {
        cleanExpired();
        return storage.get(key);
    }

    public synchronized void clear() {
        storage.clear();
    }

    private void cleanExpired() {
        long now = System.currentTimeMillis();
        storage.entrySet().removeIf(e -> e.getValue().expiresAtEpoch() < now);
    }
}
