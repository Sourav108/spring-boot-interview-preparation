package com.spring.interview.foundations.context;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Architectural comparison demonstrating lazy BeanFactory vs eager ApplicationContext instantiation.
 */
public class ApplicationContextComparisonDemo {

    public static class SampleEagerBean {
        private static final AtomicBoolean instantiated = new AtomicBoolean(false);

        public SampleEagerBean() {
            instantiated.set(true);
        }

        public static boolean isInstantiated() {
            return instantiated.get();
        }

        public static void reset() {
            instantiated.set(false);
        }
    }

    public static boolean testBeanFactoryLazyBehavior() {
        SampleEagerBean.reset();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("sampleBean", new RootBeanDefinition(SampleEagerBean.class));

        // Before getBean() -> should NOT be instantiated
        boolean instantiatedBefore = SampleEagerBean.isInstantiated();

        // After getBean() -> must be instantiated
        factory.getBean("sampleBean");
        boolean instantiatedAfter = SampleEagerBean.isInstantiated();

        return !instantiatedBefore && instantiatedAfter;
    }

    public static boolean testApplicationContextEagerBehavior() {
        SampleEagerBean.reset();
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(SampleEagerBean.class);
            context.refresh(); // Eager preInstantiateSingletons occurs here

            // Immediately after refresh, before any getBean() call -> already instantiated
            return SampleEagerBean.isInstantiated();
        }
    }
}
