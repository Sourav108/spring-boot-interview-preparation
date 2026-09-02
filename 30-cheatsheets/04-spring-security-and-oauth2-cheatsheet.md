# 30-04: Spring Security & OAuth 2.1 Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-04`
> **Primary Technology**: Spring Security 6.4 | OAuth 2.1 & JWT
> **Verification Date**: 2026-09-01

---

## 🛡️ Production `SecurityFilterChain` Template (Lambda DSL)
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health/**", "/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
}
```
