package com.spring.interview.migration.callback;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Flyway callback intercepting migration lifecycle events.
 */
@Component
public class AuditMigrationCallback implements Callback {

    private static final Logger log = LoggerFactory.getLogger(AuditMigrationCallback.class);
    private final List<Event> observedEvents = new ArrayList<>();

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.BEFORE_MIGRATE || event == Event.AFTER_MIGRATE;
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return false;
    }

    @Override
    public void handle(Event event, Context context) {
        observedEvents.add(event);
        log.info("Flyway Lifecycle Event: {}", event.name());
    }

    @Override
    public String getCallbackName() {
        return "AuditMigrationCallback";
    }

    public List<Event> getObservedEvents() {
        return Collections.unmodifiableList(observedEvents);
    }
}
