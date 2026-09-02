package com.spring.interview.lifecycle.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

class OrderEventPublisherTest {

    @Configuration
    @Import({OrderEventPublisher.OrderPlacementService.class, OrderEventPublisher.OrderAuditEventListener.class})
    static class EventsConfig {}

    @Test
    @DisplayName("Should publish domain events and capture them synchronously in listener")
    void shouldPublishAndListenToDomainEvents() {
        try (var context = new AnnotationConfigApplicationContext(EventsConfig.class)) {
            var service = context.getBean(OrderEventPublisher.OrderPlacementService.class);
            var listener = context.getBean(OrderEventPublisher.OrderAuditEventListener.class);

            service.placeOrder("ord-99", 250.0);

            assertThat(listener.getCapturedEvents()).hasSize(1);
            assertThat(listener.getCapturedEvents().getFirst().orderId()).isEqualTo("ord-99");
            assertThat(listener.getCapturedEvents().getFirst().amount()).isEqualTo(250.0);
        }
    }
}
