# 16-04: Custom JwtAuthenticationConverter & Nested Role Claim Mapping

> **Module**: `MOD-16: OAuth 2.0 & JWT`
> **Topic ID**: `SB-16-04`
> **Prerequisites**: `SB-15-03`, `SB-16-03`
> **Primary Technology**: Java 21 LTS | JWT Converter | Keycloak / Auth0 Claim Mapping
> **Verification Date**: 2026-09-01

---

## 1. Problem
By default, Spring Security maps JWT claims into `GrantedAuthority` using the standard `scope` or `scp` claim with the `SCOPE_` prefix (e.g. `SCOPE_read`). However, identity providers like Keycloak store user roles in nested JSON structures (`realm_access.roles: ["admin", "editor"]`), causing `@PreAuthorize("hasRole('ADMIN')")` checks to fail!

---

## 2. Why It Exists
Spring Security provides the **`Converter<Jwt, AbstractAuthenticationToken>`** SPI. By plugging a custom **`JwtAuthenticationConverter`**, developers can extract roles from arbitrary claim paths and map them to standard Spring Security `ROLE_` prefixed authorities.

---

## 3. Production Example in Java 21: Keycloak Role Converter

```java
package com.spring.interview.oauth2.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractRealmRoles(jwt);
        String principalClaimName = jwt.getClaimAsString("preferred_username");
        if (principalClaimName == null) {
            principalClaimName = jwt.getSubject();
        }
        return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .collect(Collectors.toSet());
    }
}
```

---

## 4. Wiring the Converter into the SecurityFilterChain
```java
@Bean
public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    KeycloakJwtAuthenticationConverter jwtConverter
) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
        )
        .build();
}
```

---

## 5. Common Mistakes
- **Prefix mismatch between `hasRole('ADMIN')` and `hasAuthority('ROLE_ADMIN')`**: `hasRole('ADMIN')` automatically appends the `ROLE_` prefix under the hood. If your converter produces `ADMIN` without `ROLE_`, `hasRole('ADMIN')` will fail!

---

## 6. Interview Questions
1. **SDE2**: What is the difference between `hasRole('USER')` and `hasAuthority('USER')` in Spring Security?
2. **Senior**: How do you extract and map nested roles from complex JWT structures (like Keycloak `realm_access` or Auth0 permissions) in Spring Boot 3?

---

## 7. Interview Answer (Senior Level)
"`hasRole('USER')` automatically prepends the default prefix `ROLE_` and looks for authority `ROLE_USER`, whereas `hasAuthority('USER')` checks the raw authority string without prefixing. To map nested claims from IdPs like Keycloak, we implement `Converter<Jwt, AbstractAuthenticationToken>` (or configure `JwtGrantedAuthoritiesConverter`). The custom converter inspects the `realm_access.roles` JSON map in the `Jwt` claims, maps each role to a `SimpleGrantedAuthority("ROLE_" + role.toUpperCase())`, extracts the `preferred_username` claim as principal name, and returns a `JwtAuthenticationToken`. We then attach this converter to the Resource Server DSL via `.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(myConverter)))`."
