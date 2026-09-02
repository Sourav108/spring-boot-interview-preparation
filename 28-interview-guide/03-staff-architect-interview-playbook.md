# 28-03: Staff / Principal Architect Spring Boot Interview Playbook

> **Target Role**: Staff Software Engineer / Principal Architect / Lead Backend Engineer
> **Key Focus Areas**: Modular Monoliths vs Microservices, Multi-Tenancy Topologies, Distributed Sagas & Outbox, JVM & HikariCP Physics, SRE Observability Signals
> **Verification Date**: 2026-09-01

---

## 🎯 What Interviewers Look For at Staff / Architect Level
1. **System Topology & Trade-Off Defense**: Defending Modular Monoliths (Spring Modulith) vs Microservices with real operational math, cost models, and team boundaries.
2. **Data Consistency in Distributed Systems**: Solving the Dual-Write dilemma with Transactional Outbox & Debezium CDC, and orchestrating distributed Sagas with compensating rollbacks.
3. **Multi-Tenancy at Enterprise Scale**: Designing Schema-per-Tenant or Database-per-Tenant SaaS architectures with `AbstractRoutingDataSource` and connection pooling isolation.
4. **Performance & Hardware Physics**: Sizing HikariCP connection pools using database CPU core formulas ($T_N = C \times 2 + I$), GC selection (Generational ZGC vs G1), and Java 21 Virtual Thread architectures.

---

## 📋 The 10 Most Common Staff Architect Interview Questions & Answers

### 1. How do you resolve the Dual-Write problem between PostgreSQL and Apache Kafka?
**Answer**: "A dual-write occurs when a service mutates a database and publishes an event to Kafka in separate operations, where failure in either step leaves the system in an inconsistent state. We solve this using the **Transactional Outbox Pattern**: the service writes the business entity and the domain event into an `outbox_events` table inside the *same local ACID database transaction*. A Change Data Capture (CDC) engine like Debezium tails PostgreSQL's Write-Ahead Log (WAL) and streams events to Kafka with zero dual-write risk and At-Least-Once delivery guarantees."

### 2. When should a company choose Spring Modulith over a Microservices architecture?
**Answer**: "Spring Modulith is optimal when engineering teams are under 50 developers and domain workloads do not require drastically asymmetric hardware scaling. A Modular Monolith enforces compile-time and test-time package boundary verification (`ApplicationModules.verify()`) and uses in-memory transactional event registries. This achieves the modular decoupling and clean domain boundaries of microservices while completely eliminating distributed network latency, cross-service debugging complexity, JSON serialization overhead, and Kubernetes infrastructure costs."

### 3. How do you size HikariCP database connection pools on a multi-pod cluster?
**Answer**: "Connection pools should never be arbitrarily oversized. According to PostgreSQL and HikariCP hardware benchmarks, the optimal pool capacity is: $\text{Pool Size} = (\text{DB CPU Cores} \times 2) + \text{Disk Spindles}$. For an 8-core DB with SSD, the optimal cluster-wide connection limit is $8 \times 2 + 1 = 17$ connections. If running 4 application pods, each pod should be allocated $17 / 4 \approx 4$ connections. Sizing beyond this threshold forces the database kernel to waste CPU cycles on thread context switching and disk I/O queue contention, degrading overall throughput."

### 4. How do you architect a secure Schema-per-Tenant multi-tenant SaaS backend?
**Answer**: "We configure Hibernate's `MultiTenancyStrategy.SCHEMA` with a custom `CurrentTenantIdentifierResolver` and `MultiTenantConnectionProvider`. When an HTTP request enters the gateway, a security filter verifies the JWT, extracts the cryptographically signed `tenant_id` claim, and binds it to a `ThreadLocal` `TenantContextHolder` and SLF4J MDC. On connection checkout, the connection provider executes `SET search_path TO tenant_id`, ensuring all JPA and SQL queries are physically scoped to that tenant's schema."

### 5. How did Java 21 Virtual Threads change the architectural trade-off between Spring MVC and Spring WebFlux?
**Answer**: "Prior to Java 21, achieving 50,000+ concurrent connections required Spring WebFlux and Netty event loops to bypass Tomcat's 200-thread OS limit, despite the steep learning curve and lack of JPA support. Java 21 Virtual Threads eliminated this concurrency limitation: Spring MVC can now handle 100,000+ concurrent requests on standard imperative code by simply setting `spring.threads.virtual.enabled=true`. Today, use **Spring MVC + Virtual Threads** for standard enterprise REST APIs, database transactions, and JPA ecosystems. Reserve **Spring WebFlux** strictly for domain problems requiring continuous data streaming (SSE, WebSockets), event-driven gateways (Spring Cloud Gateway), or explicit reactive backpressure flow control."
