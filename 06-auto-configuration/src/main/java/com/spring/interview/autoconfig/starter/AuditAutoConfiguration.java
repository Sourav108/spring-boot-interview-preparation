package com.spring.interview.autoconfig.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Production auto-configuration demonstrating conditional bean creation and back-off.
 */
@AutoConfiguration
@ConditionalOnClass(AuditAutoConfiguration.AuditService.class)
@ConditionalOnProperty(name = "app.audit.enabled", havingValue = "true", matchIfMissing = true)
public class AuditAutoConfiguration {

    public interface AuditService {
        String logAudit(String event);
    }

    public static class DefaultAuditService implements AuditService {
        @Override
        public String logAudit(String event) {
            return "DEFAULT_AUDIT_LOG: " + event;
        }
    }

    @Bean
    @ConditionalOnMissingBean(AuditService.class)
    public AuditService defaultAuditService() {
        return new DefaultAuditService();
    }
}
