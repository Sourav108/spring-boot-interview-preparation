package com.spring.interview.boot.lifecycle;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Production listener capturing SpringApplication lifecycle state transitions.
 */
@Component
public class StartupLifecycleEventListener {

    private final List<String> eventSequence = new ArrayList<>();

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        eventSequence.add("APPLICATION_STARTED");
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        eventSequence.add("APPLICATION_READY");
    }

    public List<String> getEventSequence() {
        return Collections.unmodifiableList(eventSequence);
    }
}
