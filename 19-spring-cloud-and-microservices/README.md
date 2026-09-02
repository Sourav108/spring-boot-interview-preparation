# Module 19: Spring Cloud & Microservices

> **Module Code**: `MOD-19`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | Spring Cloud 2024.0.0 | OpenFeign & Gateway | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master enterprise microservices design patterns in Spring Cloud: API Gateway (Spring Cloud Gateway on Project Reactor / Netty), BFF topologies, service discovery registries (Eureka / Kubernetes DNS), Spring Cloud LoadBalancer client-side routing and zone-preference algorithms, OpenFeign declarative REST clients (`@FeignClient`), custom `ErrorDecoder` HTTP exception mapping, `RequestInterceptor` correlation ID propagation, centralized configuration management with `@RefreshScope` CGLIB proxy reloading, and the architectural trade-offs between synchronous RPC (REST/gRPC) and asynchronous event-driven messaging (Kafka).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-19-01** | [`01-microservices-patterns-api-gateway-and-service-discovery.md`](./01-microservices-patterns-api-gateway-and-service-discovery.md) | API Gateway vs BFF, Spring Cloud Gateway Netty routing, and service registry discovery. |
| **SB-19-02** | [`02-openfeign-declarative-clients-interceptors-and-errordecoder.md`](./02-openfeign-declarative-clients-interceptors-and-errordecoder.md) | Declarative Feign proxies, `RequestInterceptor` MDC correlation headers, and custom `ErrorDecoder`. |
| **SB-19-03** | [`03-spring-cloud-loadbalancer-and-client-side-routing.md`](./03-spring-cloud-loadbalancer-and-client-side-routing.md) | Client-side vs Server-side load balancing, Round-Robin, and Zone-Preference cloud egress optimization. |
| **SB-19-04** | [`04-distributed-configuration-and-refreshscope-mechanics.md`](./04-distributed-configuration-and-refreshscope-mechanics.md) | Spring Cloud Config, `@RefreshScope` CGLIB dynamic proxy target eviction, and `/actuator/refresh`. |
| **SB-19-05** | [`05-synchronous-vs-asynchronous-microservice-communication.md`](./05-synchronous-vs-asynchronous-microservice-communication.md) | Temporal coupling, availability multiplication math ($A_1 \times A_2$), and asynchronous decoupling. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/cloud/`](./src/main/java/com/spring/interview/cloud/):

```
19-spring-cloud-and-microservices/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/cloud/
    │   ├── client/PaymentGatewayFeignClient.java                # Declarative OpenFeign client interface
    │   ├── decoder/CustomFeignErrorDecoder.java                 # Custom ErrorDecoder translating HTTP 4xx/5xx
    │   ├── interceptor/FeignCorrelationIdInterceptor.java       # RequestInterceptor propagating X-Correlation-Id
    │   └── SpringCloudApplication.java                          # Executable application entrypoint with @EnableFeignClients
    └── test/java/com/spring/interview/cloud/                    # 100% Mocked Tier Test Suite (3 Unit Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
