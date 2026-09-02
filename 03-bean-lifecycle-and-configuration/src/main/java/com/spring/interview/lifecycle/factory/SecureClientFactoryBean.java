package com.spring.interview.lifecycle.factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * FactoryBean implementation encapsulating complex cryptographic client initialization.
 */
public class SecureClientFactoryBean implements FactoryBean<SecureClientFactoryBean.SecureRemoteClient> {

    public record SecureRemoteClient(String endpointUrl, String apiKey, boolean isTlsActive) {
        public String query(String payload) {
            return "SECURE[" + endpointUrl + "]: " + payload;
        }
    }

    private final String endpointUrl;
    private final String apiKey;

    public SecureClientFactoryBean(String endpointUrl, String apiKey) {
        this.endpointUrl = endpointUrl;
        this.apiKey = apiKey;
    }

    @Override
    public SecureRemoteClient getObject() {
        // Multi-step complex client configuration
        return new SecureRemoteClient(endpointUrl, apiKey, true);
    }

    @Override
    public Class<?> getObjectType() {
        return SecureRemoteClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
