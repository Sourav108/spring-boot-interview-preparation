package com.spring.interview.foundations.mini;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Educational Mini-IoC Container demonstrating constructor dependency injection,
 * singleton lifecycle management, and circular dependency detection in pure Java 21.
 *
 * (Educational simplification — not a Spring replacement).
 */
public class MiniIocContainer {

    private final Map<Class<?>, Object> singletonRegistry = new ConcurrentHashMap<>();
    private final Set<Class<?>> currentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public synchronized <T> void registerSingleton(Class<T> type, T instance) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(instance, "instance must not be null");
        singletonRegistry.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T getBean(Class<T> requiredType) {
        Objects.requireNonNull(requiredType, "requiredType must not be null");

        // 1. Return from singleton cache if already initialized
        if (singletonRegistry.containsKey(requiredType)) {
            return (T) singletonRegistry.get(requiredType);
        }

        // 2. Circular dependency check
        if (currentlyInCreation.contains(requiredType)) {
            throw new IllegalStateException("Circular dependency detected while creating bean of type: " + requiredType.getName());
        }

        currentlyInCreation.add(requiredType);

        try {
            // 3. Find primary constructor
            Constructor<?>[] constructors = requiredType.getDeclaredConstructors();
            if (constructors.length == 0) {
                throw new IllegalStateException("No constructor found for: " + requiredType.getName());
            }

            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] args = new Object[paramTypes.length];

            // 4. Recursively resolve constructor parameter dependencies
            for (int i = 0; i < paramTypes.length; i++) {
                args[i] = getBean(paramTypes[i]);
            }

            // 5. Instantiate bean
            T beanInstance = (T) constructor.newInstance(args);

            // 6. Register into singleton cache
            singletonRegistry.put(requiredType, beanInstance);
            return beanInstance;

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate bean of type: " + requiredType.getName(), e);
        } finally {
            currentlyInCreation.remove(requiredType);
        }
    }

    public boolean containsBean(Class<?> type) {
        return singletonRegistry.containsKey(type);
    }

    public int getRegisteredBeanCount() {
        return singletonRegistry.size();
    }
}
