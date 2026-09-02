package com.spring.interview.oauth2.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakJwtAuthenticationConverterTest {

    private final KeycloakJwtAuthenticationConverter converter = new KeycloakJwtAuthenticationConverter();

    @Test
    @DisplayName("Should extract nested realm roles from JWT claim and map to ROLE_ prefixed authorities")
    void shouldExtractAndMapRealmRoles() {
        Jwt jwt = new Jwt(
            "fake-token-value",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Map.of("alg", "RS256", "kid", "auth-key-1"),
            Map.of(
                "sub", "usr-abc-123",
                "preferred_username", "alice_architect",
                "realm_access", Map.of("roles", List.of("admin", "manager", "auditor"))
            )
        );

        AbstractAuthenticationToken token = converter.convert(jwt);

        assertThat(token).isNotNull();
        assertThat(token.getName()).isEqualTo("alice_architect");

        List<String> authorityStrings = token.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        assertThat(authorityStrings).containsExactlyInAnyOrder(
            "ROLE_ADMIN",
            "ROLE_MANAGER",
            "ROLE_AUDITOR"
        );
    }
}
