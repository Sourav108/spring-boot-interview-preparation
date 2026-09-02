# 22-04: Structured JSON Logging & SLF4J MDC Cross-Thread Context Propagation

> **Module**: `MOD-22: Observability & Production Readiness`
> **Topic ID**: `SB-22-04`
> **Prerequisites**: `SB-08-03`, `SB-22-03`
> **Primary Technology**: Java 21 LTS | SLF4J MDC | JSON Structured Logging
> **Verification Date**: 2026-09-01

---

## 1. Problem
Unstructured string logs (`"Processing order 123 for user 456"`) require fragile regex parsing in Elasticsearch/Datadog and fail to associate background worker threads with the originating HTTP request.

---

## 2. Why It Exists: SLF4J MDC (Mapped Diagnostic Context)
MDC provides a thread-local key-value map automatically appended to every log entry output by Logback/Log4j2:
- `MDC.put("traceId", traceId)`
- `MDC.put("userId", userId)`
- `MDC.put("tenantId", tenantId)`

---

## 3. Architecture: JSON Structured Log Format
Instead of plaintext, Spring Boot logs structured JSON:
```json
{
  "@timestamp": "2026-09-02T10:15:30.123Z",
  "level": "INFO",
  "logger_name": "com.spring.interview.service.OrderService",
  "thread_name": "http-nio-8080-exec-1",
  "message": "Order processed successfully",
  "traceId": "4bf92f3577b34da6a3ce929d0e0e4736",
  "spanId": "00f067aa0ba902b7",
  "orderId": "ORD-9021",
  "amount": 250.00
}
```

---

## 4. Production Example in Java 21: `TaskDecorator` for Async MDC Propagation
```java
package com.spring.interview.observability.logging;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
public class AsyncMdcConfiguration {

    public static class MdcTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            // 1. Capture MDC context from calling thread
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    // 2. Attach context to worker thread
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap);
                    }
                    runnable.run();
                } finally {
                    // 3. Prevent thread pollution on thread pool reuse!
                    MDC.clear();
                }
            };
        }
    }

    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.initialize();
        return executor;
    }
}
```

---

## 5. Common Mistakes
- **Failing to call `MDC.clear()` in a `finally` block**: In pooled thread environments (Tomcat servlet threads), uncleared MDC leaks old user IDs and trace IDs to subsequent unrelated requests processed by that reused thread.

---

## 6. Interview Questions
1. **SDE2**: Why must you call `MDC.clear()` in a servlet filter's `finally` block?
2. **Senior**: How does `TaskDecorator` prevent MDC context loss across asynchronous `@Async` thread executions?

---

## 7. Interview Answer (Senior Level)
"Because web servers (Tomcat/Jetty) reuse worker threads from a thread pool across multiple HTTP requests, failing to call `MDC.clear()` in a `finally` block leaves stale contextual keys (like `userId` or `traceId`) in the thread-local map, contaminating logs for future requests handled by that thread. To propagate MDC across `@Async` boundaries, we implement Spring's `TaskDecorator`. The decorator's `decorate()` method captures a snapshot of the caller's MDC map via `MDC.getCopyOfContextMap()` and returns a wrapped `Runnable` that sets the context on the asynchronous worker thread before execution and reliably clears it in a `finally` block upon completion."
