package com.spring.interview.ioc.circular;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service demonstrating resolution of circular references via lazy proxy injection.
 */
public class CircularDependencyResolver {

    public interface WorkerA {
        String executeA();
        String replyToB();
    }

    public interface WorkerB {
        String executeB();
        String replyToA();
    }

    @Service
    public static class ServiceWorkerA implements WorkerA {
        private final WorkerB workerB;

        public ServiceWorkerA(@Lazy WorkerB workerB) {
            this.workerB = Objects.requireNonNull(workerB, "workerB must not be null");
        }

        @Override
        public String executeA() {
            return "WorkerA -> " + workerB.replyToA();
        }

        @Override
        public String replyToB() {
            return "AckFromA";
        }
    }

    @Service
    public static class ServiceWorkerB implements WorkerB {
        private final WorkerA workerA;

        public ServiceWorkerB(WorkerA workerA) {
            this.workerA = Objects.requireNonNull(workerA, "workerA must not be null");
        }

        @Override
        public String executeB() {
            return "WorkerB -> " + workerA.replyToB();
        }

        @Override
        public String replyToA() {
            return "AckFromB";
        }
    }
}
