package com.spring.interview.autoconfig.mini;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MiniAutoConfigurationEngineTest {

    interface SecurityService {
        String authenticate();
    }

    static class DefaultSecurityService implements SecurityService {
        @Override
        public String authenticate() {
            return "DEFAULT_AUTH";
        }
    }

    static class CustomUserSecurityService implements SecurityService {
        @Override
        public String authenticate() {
            return "CUSTOM_ENTERPRISE_AUTH";
        }
    }

    @Test
    @DisplayName("Should create default bean when no user bean exists and properties match")
    void shouldCreateDefaultBeanWhenMissing() {
        var engine = new MiniAutoConfigurationEngine();
        engine.setProperty("app.security.enabled", true);

        var config = new MiniAutoConfigurationEngine.CandidateConfig(
            "SecurityAutoConfiguration",
            SecurityService.class,
            SecurityService.class,
            "app.security.enabled",
            true,
            new DefaultSecurityService()
        );

        List<MiniAutoConfigurationEngine.EvaluationResult> results = engine.evaluateAndApply(List.of(config));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().activated()).isTrue();
        assertThat(engine.getBean(SecurityService.class).authenticate()).isEqualTo("DEFAULT_AUTH");
    }

    @Test
    @DisplayName("Should back off when user has already registered a custom bean")
    void shouldBackOffWhenUserBeanPresent() {
        var engine = new MiniAutoConfigurationEngine();
        engine.registerUserBean(SecurityService.class, new CustomUserSecurityService());

        var config = new MiniAutoConfigurationEngine.CandidateConfig(
            "SecurityAutoConfiguration",
            SecurityService.class,
            SecurityService.class,
            "app.security.enabled",
            true,
            new DefaultSecurityService()
        );

        List<MiniAutoConfigurationEngine.EvaluationResult> results = engine.evaluateAndApply(List.of(config));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().activated()).isFalse();
        assertThat(results.getFirst().reason()).contains("Backed off!");
        assertThat(engine.getBean(SecurityService.class).authenticate()).isEqualTo("CUSTOM_ENTERPRISE_AUTH");
    }
}
