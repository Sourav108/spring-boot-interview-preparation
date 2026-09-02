package com.spring.interview.autoconfig.starter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class AuditAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(AuditAutoConfiguration.class));

    static class CustomAuditService implements AuditAutoConfiguration.AuditService {
        @Override
        public String logAudit(String event) {
            return "CUSTOM_USER_AUDIT: " + event;
        }
    }

    @Configuration
    static class UserAuditConfig {
        @Bean
        public AuditAutoConfiguration.AuditService userAudit() {
            return new CustomAuditService();
        }
    }

    @Test
    @DisplayName("Should auto-configure DefaultAuditService when no user bean exists")
    void shouldAutoConfigureDefaultAuditService() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(AuditAutoConfiguration.AuditService.class);
            var service = context.getBean(AuditAutoConfiguration.AuditService.class);
            assertThat(service.logAudit("LOGIN")).isEqualTo("DEFAULT_AUDIT_LOG: LOGIN");
        });
    }

    @Test
    @DisplayName("Should back off DefaultAuditService when user defines custom bean")
    void shouldBackOffWhenUserProvidesBean() {
        contextRunner.withUserConfiguration(UserAuditConfig.class)
            .run(context -> {
                assertThat(context).hasSingleBean(AuditAutoConfiguration.AuditService.class);
                var service = context.getBean(AuditAutoConfiguration.AuditService.class);
                assertThat(service.logAudit("LOGIN")).isEqualTo("CUSTOM_USER_AUDIT: LOGIN");
            });
    }
}
