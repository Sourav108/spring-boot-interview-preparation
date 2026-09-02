package com.spring.interview.aop.mini;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Educational Mini-AOP Proxy demonstrating invocation handling, before/after advice chains,
 * and target method delegation.
 *
 * (Educational simplification — not a Spring replacement).
 */
public class MiniAopProxy {

    @FunctionalInterface
    public interface BeforeAdvice {
        void before(Method method, Object[] args);
    }

    @FunctionalInterface
    public interface AfterReturningAdvice {
        void afterReturning(Object returnValue, Method method, Object[] args);
    }

    public static class ProxyFactory<T> {
        private final T target;
        private final Class<T> interfaceType;
        private final List<BeforeAdvice> beforeAdvices = new ArrayList<>();
        private final List<AfterReturningAdvice> afterAdvices = new ArrayList<>();

        public ProxyFactory(T target, Class<T> interfaceType) {
            this.target = Objects.requireNonNull(target, "target must not be null");
            this.interfaceType = Objects.requireNonNull(interfaceType, "interfaceType must not be null");
            if (!interfaceType.isInterface()) {
                throw new IllegalArgumentException("interfaceType must be an interface");
            }
        }

        public ProxyFactory<T> addBeforeAdvice(BeforeAdvice advice) {
            this.beforeAdvices.add(Objects.requireNonNull(advice));
            return this;
        }

        public ProxyFactory<T> addAfterReturningAdvice(AfterReturningAdvice advice) {
            this.afterAdvices.add(Objects.requireNonNull(advice));
            return this;
        }

        @SuppressWarnings("unchecked")
        public T createProxy() {
            InvocationHandler handler = (proxy, method, args) -> {
                // 1. Execute @Before advice chain
                for (BeforeAdvice beforeAdvice : beforeAdvices) {
                    beforeAdvice.before(method, args);
                }

                // 2. Delegate to target instance
                Object result = method.invoke(target, args);

                // 3. Execute @AfterReturning advice chain
                for (AfterReturningAdvice afterAdvice : afterAdvices) {
                    afterAdvice.afterReturning(result, method, args);
                }

                return result;
            };

            return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                handler
            );
        }
    }
}
