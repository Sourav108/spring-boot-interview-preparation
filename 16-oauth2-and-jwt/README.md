# Module 16: OAuth 2.0 & JWT

> **Module Code**: `MOD-16`
> **Status**: `COMPLETE` ✅
> **Source of Truth**: [`../CURRICULUM.md`](../CURRICULUM.md)
> **Primary Technology**: Java 21 LTS | OAuth 2.1 / OIDC | JWT Resource Server | Maven 3.9+
> **Verification Date**: 2026-09-01

---

## 🎯 Module Objective
Master distributed authentication and authorization in Spring Security 6: OAuth 2.0 / OIDC core roles (Resource Owner, Client, Authorization Server, Resource Server), modern grant flows (Authorization Code with PKCE, Client Credentials, Refresh Tokens), the RFC 7519 JSON Web Token structure (Header, Payload, Signature), RS256 asymmetric cryptographic verification via JWKS (`/.well-known/jwks.json`), configuring a stateless Spring Security Resource Server with `NimbusJwtDecoder`, custom `JwtAuthenticationConverter` implementations for nested IdP claims (Keycloak `realm_access.roles`), and production token hygiene (short-lived access tokens, Refresh Token Rotation, distributed revocation blacklists).

---

## 📚 5 In-Depth Technical Lessons

| ID | Lesson Title | Key Focus & Deliverables |
|:---:|---|---|
| **SB-16-01** | [`01-oauth2-architecture-roles-and-grant-types.md`](./01-oauth2-architecture-roles-and-grant-types.md) | The 4 OAuth roles, Authorization Code + PKCE, Client Credentials, and deprecated flows. |
| **SB-16-02** | [`02-jwt-internals-header-payload-signature-and-jwks.md`](./02-jwt-internals-header-payload-signature-and-jwks.md) | JWT RFC 7519 structure, Symmetric (HS256) vs Asymmetric (RS256), and JWKS key rotation. |
| **SB-16-03** | [`03-spring-security-oauth2-resource-server-configuration.md`](./03-spring-security-oauth2-resource-server-configuration.md) | Resource Server architecture, `BearerTokenAuthenticationFilter`, and `NimbusJwtDecoder`. |
| **SB-16-04** | [`04-custom-jwt-authentication-converter-and-role-mapping.md`](./04-custom-jwt-authentication-converter-and-role-mapping.md) | Custom `JwtAuthenticationConverter` mapping nested Keycloak claims to `ROLE_` authorities. |
| **SB-16-05** | [`05-token-revocation-refresh-tokens-and-security-hygiene.md`](./05-token-revocation-refresh-tokens-and-security-hygiene.md) | Refresh Token Rotation, reuse compromise detection, short-lived tokens, and Redis blacklists. |

---

## 💻 Java 21 Reference Implementations

The module includes runnable, tested Java 21 code in [`src/main/java/com/spring/interview/oauth2/`](./src/main/java/com/spring/interview/oauth2/):

```
16-oauth2-and-jwt/
├── pom.xml
└── src/
    ├── main/java/com/spring/interview/oauth2/
    │   ├── config/OAuth2ResourceServerConfiguration.java         # Resource server configuration with custom JWT converter
    │   ├── converter/KeycloakJwtAuthenticationConverter.java     # Converter mapping nested realm_access.roles to GrantedAuthorities
    │   ├── controller/ProtectedApiController.java                # Controller endpoints with @PreAuthorize("hasRole('ADMIN')")
    │   └── SpringOAuth2Application.java                          # Executable application entrypoint
    └── test/java/com/spring/interview/oauth2/                    # 100% Mocked Tier Test Suite (6 Unit & MockMvc Tests)
```

---

## 🧪 Running the Verification Test Suite

```bash
mvn clean test
```
