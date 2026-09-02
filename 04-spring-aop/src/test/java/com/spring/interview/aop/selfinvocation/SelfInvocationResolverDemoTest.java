package com.spring.interview.aop.selfinvocation;

import com.spring.interview.aop.aspects.PerformanceAuditingAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.assertj.core.api.Assertions.assertThat;

class SelfInvocationResolverDemoTest {

    @Configuration
    @EnableAspectJAutoProxy
    static class SelfTestConfig {
        @Bean
        public PerformanceAuditingAspect auditingAspect() {
            return new PerformanceAuditingAspect();
        }

        @Bean
        public SelfInvocationResolverDemo resolverDemo() {
            return new SelfInvocationResolverDemo();
        }
    }

    @Test
    @DisplayName("Should demonstrate self-invocation bypass vs self-injection resolution")
    void shouldDemonstrateSelfInvocationBypassAndFix() {
        try (var context = new AnnotationConfigApplicationContext(SelfTestConfig.class)) {
            var demo = context.getBean(SelfInvocationResolverDemo.class);
            var aspect = context.getBean(PerformanceAuditingAspect.class);

            // 1. Direct internal 'this' call bypasses the proxy
            demo.executeDirectInternalCall();
            assertThat(aspect.getInvocationCount("annotatedTargetMethod")).isEqualTo(0);

            // 2. Wire the proxy to 'self'
            demo.setSelf(demo);

            // 3. Proxied internal call hits the aspect
            demo.executeProxiedInternalCall();
            assertThat(aspect.getInvocationCount("annotatedTargetMethod")).isEqualTo(1);
        }
    }
}
