# 30-06: Resilience4j Fault Tolerance Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-06`
> **Primary Technology**: Resilience4j 2.2.0 | Spring Boot 3.4
> **Verification Date**: 2026-09-01

---

## ⚡ Circuit Breaker & Retry Annotations
```java
@Service
public class PaymentResilienceClient {

    @Retry(name = "paymentService", fallbackMethod = "handlePaymentFallback")
    @CircuitBreaker(name = "paymentService")
    @RateLimiter(name = "paymentService")
    public PaymentResult executePayment(PaymentRequest request) {
        return externalPaymentGateway.charge(request);
    }

    public PaymentResult handlePaymentFallback(PaymentRequest request, Throwable ex) {
        return PaymentResult.failedFallback("Gateway unavailable: " + ex.getMessage());
    }
}
```

> **Aspect Order**: `Retry (Outer) -> CircuitBreaker -> RateLimiter -> TimeLimiter -> Bulkhead (Inner)`.
