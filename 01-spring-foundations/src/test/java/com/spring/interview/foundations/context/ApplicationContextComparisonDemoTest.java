package com.spring.interview.foundations.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationContextComparisonDemoTest {

    @Test
    @DisplayName("Should verify BeanFactory initializes beans lazily on getBean()")
    void shouldVerifyBeanFactoryLazyBehavior() {
        boolean lazySuccess = ApplicationContextComparisonDemo.testBeanFactoryLazyBehavior();
        assertThat(lazySuccess).isTrue();
    }

    @Test
    @DisplayName("Should verify ApplicationContext initializes singletons eagerly at refresh()")
    void shouldVerifyApplicationContextEagerBehavior() {
        boolean eagerSuccess = ApplicationContextComparisonDemo.testApplicationContextEagerBehavior();
        assertThat(eagerSuccess).isTrue();
    }
}
