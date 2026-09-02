package com.spring.interview.ioc.circular;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

class CircularDependencyResolverTest {

    @Configuration
    @Import({CircularDependencyResolver.ServiceWorkerA.class, CircularDependencyResolver.ServiceWorkerB.class})
    static class TestConfig {}

    @Test
    @DisplayName("Should resolve circular dependency at runtime using @Lazy proxy injection")
    void shouldResolveCircularDependencyViaLazy() {
        try (var context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            var workerA = context.getBean(CircularDependencyResolver.ServiceWorkerA.class);
            var workerB = context.getBean(CircularDependencyResolver.ServiceWorkerB.class);

            assertThat(workerA.executeA()).isEqualTo("WorkerA -> AckFromB");
            assertThat(workerB.executeB()).isEqualTo("WorkerB -> AckFromA");
        }
    }
}
