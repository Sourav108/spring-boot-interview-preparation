# 26-03: Multi-Tenancy Architectures: Database vs Schema vs Discriminator

> **Module**: `MOD-26: Production Architecture`
> **Topic ID**: `SB-26-03`
> **Prerequisites**: `SB-11-01`, `SB-15-01`
> **Primary Technology**: Java 21 LTS | Multi-Tenant Architecture | AbstractRoutingDataSource
> **Verification Date**: 2026-09-01

---

## 1. Problem
In multi-tenant SaaS applications (e.g. Shopify, Slack), how do you guarantee data isolation between enterprise customers while optimizing infrastructure costs, connection pooling, and schema migration maintenance?

---

## 2. The 3 Multi-Tenancy Topologies Compared

```mermaid
flowchart TD
    Topologies{"Multi-Tenancy Topologies"}

    Topologies -->|1. Database-per-Tenant 🛡️ Max Isolation| T1["Each tenant gets a separate physical PostgreSQL database. <b>Highest security & compliance, higher infra cost.</b>"]

    Topologies -->|2. Schema-per-Tenant 🏆 SaaS Standard| T2["Shared database instance; each tenant has a separate schema (e.g. 'tenant_acme', 'tenant_globex'). <b>Great balance.</b>"]

    Topologies -->|3. Discriminator Column (Table-per-Tenant) ⚡ Lowest Cost| T3["Shared tables with 'tenant_id' column on every row. <b>Lowest cost, highest risk of developer data leakage bugs!</b>"]
```

---

## 3. Comprehensive Architectural Comparison Matrix

| Dimension | Database-per-Tenant | Schema-per-Tenant | Discriminator Column |
|---|:---:|:---:|:---:|
| **Data Isolation** | **Complete Physical Isolation 🛡️** | Logical Schema Isolation | Row-level Soft Isolation |
| **Infra Cost** | High ($$ Database instances) | Moderate | **Lowest ($)** |
| **Connection Pooling** | Complex (Pool per DB) | Moderate | **Simple (Single HikariCP pool)** |
| **Cross-Tenant Leakage Risk** | **0% (Physical impossible)** | Near 0% | High (If developer omits `WHERE tenant_id = :id`) |
| **Schema Migration** | $N$ database migrations | $N$ schema migrations | **Single standard migration ⚡** |
| **Compliance (HIPAA / GDPR)** | **Easiest to certify & delete** | Certified | Complex soft deletion |

---

## 4. Dynamic Routing in Spring: `AbstractRoutingDataSource`
```java
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenantId(); // Extracted from JWT / Subdomain
    }
}
```

---

## 5. Common Mistakes
- **Relying purely on application code for Discriminator Column isolation without PostgreSQL Row-Level Security (RLS)**: A single missed `WHERE tenant_id = ?` query exposes all customers' data. Always back discriminator models with PostgreSQL RLS policies.

---

## 6. Interview Questions
1. **SDE2**: What is `AbstractRoutingDataSource` and how does it support multi-tenancy in Spring Boot?
2. **Senior**: How do you architect a secure Schema-per-Tenant SaaS backend in Spring Boot with Hibernate Multi-Tenancy?

---

## 7. Interview Answer (Senior Level)
"`AbstractRoutingDataSource` acts as a proxy `DataSource` that dynamically resolves target database connection pools at runtime by invoking `determineCurrentLookupKey()`, which retrieves the current tenant identifier from a `ThreadLocal` context set by an authentication filter. For Schema-per-Tenant architectures, we configure Hibernate's `MultiTenancyStrategy.SCHEMA` with a `CurrentTenantIdentifierResolver` and a `MultiTenantConnectionProvider`. When a tenant initiates an HTTP request, the filter inspects the JWT tenant claim or subdomain, sets the tenant context, and Hibernate executes `SET search_path TO tenant_id` on checkout of the JDBC connection, ensuring queries are scoped strictly to that tenant's schema."
