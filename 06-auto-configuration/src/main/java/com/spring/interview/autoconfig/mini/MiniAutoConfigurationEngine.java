package com.spring.interview.autoconfig.mini;

import java.util.*;

/**
 * Educational Mini-AutoConfiguration Engine demonstrating conditional evaluation,
 * user-bean precedence, and auto-configuration back-off mechanics in pure Java 21.
 *
 * (Educational simplification — not a Spring replacement).
 */
public class MiniAutoConfigurationEngine {

    public record CandidateConfig(
        String configName,
        Class<?> requiredClassOnClasspath,
        Class<?> targetBeanType,
        String propertyToggleKey,
        boolean defaultPropertyToggleValue,
        Object defaultBeanInstance
    ) {}

    public record EvaluationResult(
        String configName,
        boolean activated,
        String reason
    ) {}

    private final Set<Class<?>> userDefinedBeans = new HashSet<>();
    private final Map<String, Boolean> environmentProperties = new HashMap<>();
    private final Map<Class<?>, Object> finalBeanRegistry = new HashMap<>();

    public void registerUserBean(Class<?> type, Object instance) {
        userDefinedBeans.add(type);
        finalBeanRegistry.put(type, instance);
    }

    public void setProperty(String key, boolean value) {
        environmentProperties.put(key, value);
    }

    public List<EvaluationResult> evaluateAndApply(List<CandidateConfig> autoConfigs) {
        List<EvaluationResult> reports = new ArrayList<>();

        for (CandidateConfig config : autoConfigs) {
            // 1. Check Classpath Condition (@ConditionalOnClass)
            if (config.requiredClassOnClasspath() != null) {
                try {
                    Class.forName(config.requiredClassOnClasspath().getName());
                } catch (ClassNotFoundException e) {
                    reports.add(new EvaluationResult(config.configName(), false, "Missing required class on classpath: " + config.requiredClassOnClasspath().getName()));
                    continue;
                }
            }

            // 2. Check Property Condition (@ConditionalOnProperty)
            if (config.propertyToggleKey() != null) {
                boolean isEnabled = environmentProperties.getOrDefault(config.propertyToggleKey(), config.defaultPropertyToggleValue());
                if (!isEnabled) {
                    reports.add(new EvaluationResult(config.configName(), false, "Property '" + config.propertyToggleKey() + "' evaluated to false"));
                    continue;
                }
            }

            // 3. Check Bean Missing Condition (@ConditionalOnMissingBean) - Back-off!
            if (userDefinedBeans.contains(config.targetBeanType())) {
                reports.add(new EvaluationResult(config.configName(), false, "Backed off! User-defined bean of type " + config.targetBeanType().getSimpleName() + " takes precedence"));
                continue;
            }

            // 4. All conditions matched: Register default bean
            finalBeanRegistry.put(config.targetBeanType(), config.defaultBeanInstance());
            reports.add(new EvaluationResult(config.configName(), true, "All conditions matched. Default bean created."));
        }

        return reports;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) finalBeanRegistry.get(type);
    }

    public boolean hasBean(Class<?> type) {
        return finalBeanRegistry.containsKey(type);
    }
}
