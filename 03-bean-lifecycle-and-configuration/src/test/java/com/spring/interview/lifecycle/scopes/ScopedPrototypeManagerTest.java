package com.spring.interview.lifecycle.scopes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

class ScopedPrototypeManagerTest {

    @Configuration
    @Import({ScopedPrototypeManager.TaskWorker.class, ScopedPrototypeManager.SingletonTaskManager.class})
    static class ScopesConfig {}

    @Test
    @DisplayName("Should generate distinct prototype instances across consecutive ObjectProvider calls")
    void shouldGenerateDistinctPrototypes() {
        try (var context = new AnnotationConfigApplicationContext(ScopesConfig.class)) {
            var manager = context.getBean(ScopedPrototypeManager.SingletonTaskManager.class);

            var worker1 = manager.acquireFreshWorker();
            var worker2 = manager.acquireFreshWorker();

            assertThat(worker1).isNotSameAs(worker2);
            assertThat(worker1.getWorkerId()).isNotEqualTo(worker2.getWorkerId());
        }
    }
}
