package com.spring.interview.lifecycle.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class SecureClientFactoryBeanTest {

    @Configuration
    static class FactoryConfig {
        @Bean
        public SecureClientFactoryBean secureClient() {
            return new SecureClientFactoryBean("https://api.vault.internal", "key-secret-123");
        }
    }

    @Test
    @DisplayName("Should return FactoryBean.getObject() on direct lookup, and FactoryBean on '&' prefix")
    void shouldVerifyFactoryBeanSemantics() {
        try (var context = new AnnotationConfigApplicationContext(FactoryConfig.class)) {
            // 1. Direct name returns target object
            Object clientObj = context.getBean("secureClient");
            assertThat(clientObj).isInstanceOf(SecureClientFactoryBean.SecureRemoteClient.class);
            var client = (SecureClientFactoryBean.SecureRemoteClient) clientObj;
            assertThat(client.endpointUrl()).isEqualTo("https://api.vault.internal");
            assertThat(client.query("test")).contains("SECURE[https://api.vault.internal]");

            // 2. Prefix '&' returns the FactoryBean itself
            Object factoryObj = context.getBean("&secureClient");
            assertThat(factoryObj).isInstanceOf(SecureClientFactoryBean.class);
        }
    }
}
