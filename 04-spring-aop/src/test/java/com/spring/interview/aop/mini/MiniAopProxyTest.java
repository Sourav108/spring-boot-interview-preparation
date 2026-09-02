package com.spring.interview.aop.mini;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MiniAopProxyTest {

    interface CalculationService {
        int add(int a, int b);
    }

    static class CalculationServiceImpl implements CalculationService {
        @Override
        public int add(int a, int b) {
            return a + b;
        }
    }

    @Test
    @DisplayName("Should execute Before and AfterReturning advice chains around target method invocation")
    void shouldExecuteAdviceChain() {
        CalculationService target = new CalculationServiceImpl();
        List<String> auditLog = new ArrayList<>();

        CalculationService proxy = new MiniAopProxy.ProxyFactory<>(target, CalculationService.class)
            .addBeforeAdvice((method, args) -> auditLog.add("BEFORE:" + method.getName() + ":" + args[0] + "+" + args[1]))
            .addAfterReturningAdvice((result, method, args) -> auditLog.add("AFTER:" + method.getName() + "=" + result))
            .createProxy();

        int sum = proxy.add(10, 20);

        assertThat(sum).isEqualTo(30);
        assertThat(auditLog).containsExactly(
            "BEFORE:add:10+20",
            "AFTER:add=30"
        );
    }
}
