# 15-04: Web Defense: CSRF, CORS & OWASP Security Headers

> **Module**: `MOD-15: Spring Security`
> **Topic ID**: `SB-15-04`
> **Prerequisites**: `SB-15-01`
> **Primary Technology**: Java 21 LTS | Web Application Security | OWASP Defenses
> **Verification Date**: 2026-09-01

---

## 1. Problem
Web applications are vulnerable to Cross-Site Request Forgery (CSRF), unauthorized Cross-Origin Resource Sharing (CORS) leaks, and clickjacking/MIME-sniffing attacks unless proper HTTP headers and token defenses are configured.

---

## 2. Why It Exists: CSRF vs CORS Demystified

```
CSRF: Protects SERVER from unauthorized requests sent by browser with user's stored session cookies
CORS: Relaxes BROWSER Same-Origin Policy to allow JavaScript on Domain A to read responses from Domain B
```

| Dimension | CSRF (Cross-Site Request Forgery) | CORS (Cross-Origin Resource Sharing) |
|---|---|---|
| **Threat Vector** | Malicious website triggers state-changing `POST` using user's auto-attached browser cookies | Browser blocks SPA frontend from fetching API responses from a different origin |
| **Applies to Stateless JWT APIs?** | **NO** (If tokens are passed in `Authorization: Bearer` headers) | **YES** (SPA clients running on different domains) |
| **Defense Mechanism** | Synchronizer Token Pattern (`CsrfTokenRepository`) | Response Headers (`Access-Control-Allow-Origin`, `Vary`) |

---

## 3. OWASP Security Headers Auto-Configured by Spring Security
Spring Security's `HeaderWriterFilter` appends essential security headers by default:
- **`Strict-Transport-Security` (HSTS)**: Forces HTTPS connections for 1 year (`max-age=31536000; includeSubDomains`).
- **`X-Content-Type-Options: nosniff`**: Prevents MIME-type sniffing vulnerabilities.
- **`X-Frame-Options: DENY`**: Protects against UI redressing / Clickjacking attacks.
- **`Content-Security-Policy` (CSP)**: Restricts script and resource loading sources.

---

## 4. Production CORS Configuration in Spring Security 6
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("https://app.company.com", "https://admin.company.com"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Correlation-Id"));
    config.setExposedHeaders(List.of("X-Response-Time-Millis"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L); // 1 hour preflight cache

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

---

## 5. Common Mistakes
- **Disabling CSRF on session-cookie-based web apps**: Creates an immediate critical vulnerability. (CSRF should only be disabled for stateless REST APIs using Bearer tokens).

---

## 6. Interview Questions
1. **SDE2**: When is it safe to disable CSRF in Spring Security?
2. **Senior**: How does the browser CORS preflight (`OPTIONS`) request interact with Spring Security's filter chain?

---

## 7. Interview Answer (Senior Level)
"CSRF protection is only necessary when authentication credentials (session cookies or HTTP basic auth) are automatically attached by browsers to cross-site requests. For stateless REST APIs using `Authorization: Bearer <JWT>` headers stored in client memory or `sessionStorage`, browsers do not automatically attach the token on cross-site requests, making CSRF impossible and safe to disable (`csrf.disable()`). In Spring Security, the `CorsFilter` must execute *before* `AuthorizationFilter` in the chain so that unauthenticated browser `OPTIONS` preflight requests can receive CORS validation response headers (`200 OK` / `204 No Content`) without being blocked by authorization rules."
