package com.spring.interview.aop.selfinvocation;

import com.spring.interview.aop.aspects.TrackExecutionTime;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Service demonstrating self-invocation proxy bypass and its resolution via self-injection.
 */
@Service
public class SelfInvocationResolverDemo {

    private SelfInvocationResolverDemo self;

    public void setSelf(@Lazy SelfInvocationResolverDemo self) {
        this.self = self;
    }

    // Direct 'this' call: Bypasses proxy, @TrackExecutionTime will NOT trigger
    public String executeDirectInternalCall() {
        return this.annotatedTargetMethod();
    }

    // Proxied self call: Hits proxy, @TrackExecutionTime WILL trigger
    public String executeProxiedInternalCall() {
        if (self != null) {
            return self.annotatedTargetMethod();
        }
        return annotatedTargetMethod();
    }

    @TrackExecutionTime
    public String annotatedTargetMethod() {
        return "TARGET_COMPLETED";
    }
}
