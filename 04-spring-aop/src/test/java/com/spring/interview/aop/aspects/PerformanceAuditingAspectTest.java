package com.spring.interview.aop.aspects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.assertj.core.api.Assertions.assertThat;

class PerformanceAuditingAspectTest {

    static class InstrumentedTaskService {
        @TrackExecutionTime
        public String executeHeavyTask() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
            return "DONE";
        }
    }

    @Configuration
    @EnableAspectJAutoProxy
    static class AspectTestConfig {
        @Bean
        public PerformanceAuditingAspect auditingAspect() {
            return new PerformanceAuditingAspect();
        }

        @Bean
        public InstrumentedTaskService taskService() {
            return new InstrumentedTaskService();
        }
    }

    @Test
    @DisplayName("Should intercept method annotated with @TrackExecutionTime and record execution metrics")
    void shouldProfileMethodExecution() {
        try (var context = new AnnotationConfigApplicationContext(AspectTestConfig.class)) {
            var service = context.getBean(InstrumentedTaskService.class);
            var aspect = context.getBean(PerformanceAuditingAspect.class);

            String result = service.executeHeavyTask();

            assertThat(result).isEqualTo("DONE");
            assertThat(aspect.getInvocationCount("executeHeavyTask")).isEqualTo(1);
            assertThat(aspect.getTotalDurationNanos("executeHeavyTask")).isGreaterThan(0);
        }
    }
}
