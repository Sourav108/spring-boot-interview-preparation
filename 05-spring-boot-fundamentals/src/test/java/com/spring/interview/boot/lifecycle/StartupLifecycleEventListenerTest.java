package com.spring.interview.boot.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class StartupLifecycleEventListenerTest {

    @Test
    @DisplayName("Should capture ApplicationStartedEvent and ApplicationReadyEvent in order")
    void shouldCaptureStartupEvents() {
        StartupLifecycleEventListener listener = new StartupLifecycleEventListener();

        try (var context = new AnnotationConfigApplicationContext()) {
            context.refresh();

            SpringApplication app = new SpringApplication();
            String[] args = new String[0];

            listener.onApplicationStarted(new ApplicationStartedEvent(app, args, context, Duration.ofMillis(100)));
            listener.onApplicationReady(new ApplicationReadyEvent(app, args, context, Duration.ofMillis(150)));

            assertThat(listener.getEventSequence()).containsExactly(
                "APPLICATION_STARTED",
                "APPLICATION_READY"
            );
        }
    }
}
