package com.spring.interview.resilience.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExternalPaymentResilienceServiceTest {

    @Autowired
    private ExternalPaymentResilienceService paymentService;

    @BeforeEach
    void setUp() {
        paymentService.resetCounter();
    }

    @Test
    @DisplayName("Should successfully process payment on healthy downstream call")
    void shouldProcessPaymentSuccessfully() {
        var result = paymentService.processPayment("ACC-101", 150.0);

        assertThat(result.status()).isEqualTo("SUCCESS");
        assertThat(result.transactionId()).isEqualTo("TX-ACC-101");
        assertThat(paymentService.getInvocationCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should trigger retry and invoke fallback when remote service fails")
    void shouldRetryAndInvokeFallbackOnFailure() {
        var result = paymentService.processPayment("BAD_ACCOUNT", 200.0);

        assertThat(result.status()).isEqualTo("QUEUED_OFFLINE");
        assertThat(result.transactionId()).isEqualTo("FALLBACK-BAD_ACCOUNT");
        // Max attempts = 2, so it should have been retried twice
        assertThat(paymentService.getInvocationCount()).isEqualTo(2);
    }
}
