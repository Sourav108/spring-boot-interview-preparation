package com.spring.interview.lifecycle.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class CustomValidationBeanPostProcessorTest {

    @CustomValidationBeanPostProcessor.AuditValidated
    static class AuditedService {}

    static class NormalService {}

    @Configuration
    static class TestConfig {
        @Bean
        public static CustomValidationBeanPostProcessor validationBpp() {
            return new CustomValidationBeanPostProcessor();
        }

        @Bean
        public AuditedService auditedService() {
            return new AuditedService();
        }

        @Bean
        public NormalService normalService() {
            return new NormalService();
        }
    }

    @Test
    @DisplayName("Should detect and track @AuditValidated beans via BeanPostProcessor")
    void shouldTrackAuditedBeans() {
        try (var context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            var bpp = context.getBean(CustomValidationBeanPostProcessor.class);

            assertThat(bpp.isBeanAudited("auditedService")).isTrue();
            assertThat(bpp.isBeanAudited("normalService")).isFalse();
            assertThat(bpp.getAuditedBeanCount()).isEqualTo(1);
        }
    }
}
