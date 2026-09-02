package com.spring.interview.lifecycle.scopes;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Service demonstrating safe dynamic resolution of Prototype beans within a Singleton service.
 */
public class ScopedPrototypeManager {

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class TaskWorker {
        private final String workerId = UUID.randomUUID().toString();

        public String getWorkerId() {
            return workerId;
        }
    }

    @Service
    public static class SingletonTaskManager {
        private final ObjectProvider<TaskWorker> workerProvider;

        public SingletonTaskManager(ObjectProvider<TaskWorker> workerProvider) {
            this.workerProvider = Objects.requireNonNull(workerProvider, "workerProvider must not be null");
        }

        public TaskWorker acquireFreshWorker() {
            return workerProvider.getObject();
        }
    }
}
