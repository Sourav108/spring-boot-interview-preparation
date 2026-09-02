package com.spring.interview.resilience.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service demonstrating Resilience4j @CircuitBreaker, @RateLimiter, and @Retry with fallback methods.
 */
@Service
public class ExternalPaymentResilienceService {

    private static final Logger log = LoggerFactory.getLogger(ExternalPaymentResilienceService.class);
    private final AtomicInteger invocationCounter = new AtomicInteger(0);

    public record PaymentResult(String transactionId, String status, String note) {}

    // Fallback on @Retry allows all retry attempts to execute before invoking fallback!
    @Retry(name = "paymentGateway", fallbackMethod = "paymentFallback")
    @CircuitBreaker(name = "paymentGateway")
    @RateLimiter(name = "paymentGateway", fallbackMethod = "rateLimitFallback")
    public PaymentResult processPayment(String accountId, double amount) {
        invocationCounter.incrementAndGet();
        if ("BAD_ACCOUNT".equals(accountId)) {
            throw new RuntimeException("Simulated remote gateway 500 error for account: " + accountId);
        }
        return new PaymentResult("TX-" + accountId, "SUCCESS", "Primary Gateway Success");
    }

    // Specific fallback for Rate Limiter rejections
    public PaymentResult rateLimitFallback(String accountId, double amount, RequestNotPermitted ex) {
        log.warn("Rate limit fallback triggered for account {}: {}", accountId, ex.getMessage());
        return new PaymentResult("RATE-LIMITED", "REJECTED", "Too many requests; rate limit reached");
    }

    // General fallback for Circuit Breaker OPEN or exhausted retries
    public PaymentResult paymentFallback(String accountId, double amount, Throwable ex) {
        log.warn("General payment fallback triggered for account {}: {}", accountId, ex.getMessage());
        return new PaymentResult("FALLBACK-" + accountId, "QUEUED_OFFLINE", "Queued for offline processing");
    }

    public int getInvocationCount() {
        return invocationCounter.get();
    }

    public void resetCounter() {
        invocationCounter.set(0);
    }
}
