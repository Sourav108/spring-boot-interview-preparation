package com.spring.interview.lifecycle.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom BeanPostProcessor tracking annotated beans before initialization.
 */
public class CustomValidationBeanPostProcessor implements BeanPostProcessor {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AuditValidated {}

    private final Set<String> auditedBeanNames = new HashSet<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(AuditValidated.class)) {
            auditedBeanNames.add(beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public boolean isBeanAudited(String beanName) {
        return auditedBeanNames.contains(beanName);
    }

    public int getAuditedBeanCount() {
        return auditedBeanNames.size();
    }
}
