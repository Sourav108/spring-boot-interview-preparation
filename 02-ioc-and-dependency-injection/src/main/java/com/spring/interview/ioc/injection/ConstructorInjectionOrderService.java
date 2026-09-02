package com.spring.interview.ioc.injection;

import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * Production-grade service demonstrating immutable constructor injection.
 */
public class ConstructorInjectionOrderService {

    public interface OrderRepository {
        void saveOrder(String orderId, double amount);
        boolean exists(String orderId);
    }

    public interface PaymentClient {
        boolean processPayment(String orderId, double amount);
    }

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    public ConstructorInjectionOrderService(OrderRepository orderRepository, PaymentClient paymentClient) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "orderRepository must not be null");
        this.paymentClient = Objects.requireNonNull(paymentClient, "paymentClient must not be null");
    }

    public boolean placeOrder(String orderId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Order amount must be positive");
        }
        boolean paymentSuccess = paymentClient.processPayment(orderId, amount);
        if (paymentSuccess) {
            orderRepository.saveOrder(orderId, amount);
            return true;
        }
        return false;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public PaymentClient getPaymentClient() {
        return paymentClient;
    }
}
