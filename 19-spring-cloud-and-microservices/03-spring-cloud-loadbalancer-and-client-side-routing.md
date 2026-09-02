# 19-03: Client-Side Load Balancing: Spring Cloud LoadBalancer Architecture

> **Module**: `MOD-19: Spring Cloud & Microservices`
> **Topic ID**: `SB-19-03`
> **Prerequisites**: `SB-19-01`, `SB-19-02`
> **Primary Technology**: Java 21 LTS | Spring Cloud LoadBalancer | Client-Side Routing
> **Verification Date**: 2026-09-01

---

## 1. Problem
Traditional Server-Side Load Balancing (F5 / AWS ALB) introduces an intermediate network hop for every internal service-to-service RPC call. How can a calling microservice select a healthy downstream instance directly and distribute traffic efficiently?

---

## 2. Why It Exists: Client-Side vs Server-Side Load Balancing

```mermaid
flowchart TD
    subgraph ServerSide["Server-Side Load Balancing (ALB / NGINX)"]
        A1["Service A"] --> ALB["Hardware / Cloud Load Balancer (Extra Network Hop ⚠️)"]
        ALB --> B1["Service B Pod 1"]
        ALB --> B2["Service B Pod 2"]
    end

    subgraph ClientSide["Client-Side Load Balancing (Spring Cloud LoadBalancer) 🏆"]
        A2["Service A (Maintains In-Memory Registry of Live Pod IPs)"] -->|Direct TCP (Zero extra hops!) ⚡| B3["Service B Pod 1"]
        A2 -->|Round Robin / Least Connections| B4["Service B Pod 2"]
    end
```

---

## 3. How Spring Cloud LoadBalancer Works Internally
1. **Discovery**: `DiscoveryClient` queries Eureka/Consul or Kubernetes endpoints to fetch the list of `ServiceInstance` objects for `ORDER-SERVICE`.
2. **Caching**: `ServiceInstanceListSupplier` caches the instances in memory with periodic background refresh.
3. **Selection (`ReactorServiceInstanceLoadBalancer`)**:
   - **Round-Robin** *(Default)*: Uses an `AtomicInteger` counter modulo the number of healthy instances.
   - **Random**: Chooses an instance uniformly at random.
   - **Weighted / Zone-Preference**: Prioritizes instances running within the same AWS/GCP availability zone to reduce cross-zone network latency and costs.

---

## 4. Enabling Client-Side Load Balancing on RestClient / WebClient
```java
@Bean
@LoadBalanced
public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
}

// Invocation via logical service name:
webClientBuilder.build()
    .get()
    .uri("http://PAYMENT-SERVICE/api/v1/payments")
    .retrieve();
```

---

## 5. Common Mistakes
- **Assuming `@LoadBalanced` works on un-annotated `RestTemplate` / `WebClient` instances**: `@LoadBalanced` adds a `LoadBalancerInterceptor` qualifier; without it, URI hostname `http://PAYMENT-SERVICE` fails with `UnknownHostException`.

---

## 6. Interview Questions
1. **SDE2**: What is the difference between client-side and server-side load balancing?
2. **Senior**: How does Zone-Preference routing in Spring Cloud LoadBalancer reduce cloud egress costs?

---

## 7. Interview Answer (Senior Level)
"In server-side load balancing, all traffic traverses a central hardware/cloud proxy, adding network hops and central point of failure risks. In client-side load balancing (Spring Cloud LoadBalancer), the client queries the discovery registry, caches healthy pod IP addresses, and routes TCP traffic directly to target pods using in-memory algorithms like Round-Robin. In multi-availability-zone cloud deployments (e.g. AWS `us-east-1a` and `us-east-1b`), cross-AZ data transfer incurs latency and financial egress costs. Zone-Preference routing instructs Spring Cloud LoadBalancer to prioritize downstream service instances sharing the same availability zone as the caller, falling back to other zones only when local instances are unavailable."
