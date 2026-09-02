package com.spring.interview.cloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "payment-gateway", url = "http://localhost:8089")
public interface PaymentGatewayFeignClient {

    record PaymentRequest(String accountId, double amount) {}
    record PaymentResponse(String transactionId, String status) {}

    @PostMapping("/api/v1/payments")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);

    @GetMapping("/api/v1/payments/{id}")
    PaymentResponse getPayment(@PathVariable("id") String id);
}
