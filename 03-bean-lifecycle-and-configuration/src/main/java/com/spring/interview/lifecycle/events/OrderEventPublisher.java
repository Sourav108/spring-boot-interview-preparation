package com.spring.interview.lifecycle.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain event publisher and listener harness for in-memory event-driven decoupling.
 */
public class OrderEventPublisher {

    public record OrderPlacedEvent(String orderId, double amount) {
        public OrderPlacedEvent {
            Objects.requireNonNull(orderId, "orderId must not be null");
        }
    }

    @Service
    public static class OrderPlacementService {
        private final ApplicationEventPublisher eventPublisher;

        public OrderPlacementService(ApplicationEventPublisher eventPublisher) {
            this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher must not be null");
        }

        public void placeOrder(String orderId, double amount) {
            eventPublisher.publishEvent(new OrderPlacedEvent(orderId, amount));
        }
    }

    @Component
    public static class OrderAuditEventListener {
        private final List<OrderPlacedEvent> capturedEvents = new ArrayList<>();

        @EventListener
        public void onOrderPlaced(OrderPlacedEvent event) {
            capturedEvents.add(event);
        }

        public List<OrderPlacedEvent> getCapturedEvents() {
            return Collections.unmodifiableList(capturedEvents);
        }
    }
}
