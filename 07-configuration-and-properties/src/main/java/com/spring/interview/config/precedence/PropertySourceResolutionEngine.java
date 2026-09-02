package com.spring.interview.config.precedence;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Inspection engine demonstrating Spring Boot's PropertySource resolution hierarchy.
 */
public class PropertySourceResolutionEngine {

    public record PropertySourceInfo(String name, int order, int propertyCount) {}

    public static List<PropertySourceInfo> inspectEnvironmentSources(ConfigurableEnvironment environment) {
        Objects.requireNonNull(environment, "environment must not be null");
        List<PropertySourceInfo> sourceInfos = new ArrayList<>();

        int order = 1;
        for (PropertySource<?> source : environment.getPropertySources()) {
            int count = 0;
            if (source instanceof EnumerablePropertySource<?> enumerable) {
                count = enumerable.getPropertyNames().length;
            }
            sourceInfos.add(new PropertySourceInfo(source.getName(), order++, count));
        }

        return sourceInfos;
    }
}
