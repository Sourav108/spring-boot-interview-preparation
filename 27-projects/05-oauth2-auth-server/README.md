# Project 05: Production OAuth 2.1 & OIDC Authorization Server

> **Project Code**: `PRJ-05`
> **Level**: Senior / Staff
> **Primary Technology**: Java 21 LTS | Spring Authorization Server 1.4 | RSA Key Rotation | PKCE

---

## 🏗️ Architecture & Domain Model
A standalone enterprise Identity Provider (IdP) supporting OAuth 2.1 Authorization Code Grant with PKCE, OpenID Connect 1.0 (OIDC) UserInfo, JWKS public key endpoint (`/.well-known/jwks.json`), and cryptographic RSA key rotation.

```mermaid
flowchart TD
    SPA["Single Page App (React / Mobile)"] -->|1. Auth Request with PKCE code_challenge| AuthServer["Spring Authorization Server"]
    AuthServer -->|2. User Logs In (MFA)| AuthServer
    AuthServer -->|3. Return Authorization Code| SPA
    SPA -->|4. Exchange Code + code_verifier| AuthServer
    AuthServer -->|5. Return Signed RS256 JWT Access Token + ID Token| SPA
    SPA -->|6. Bearer JWT| ResourceServer["Downstream Resource Server (Validates via JWKS) 🛡️"]
```

---

## 🔑 Key Engineering Highlights
1. **PKCE Enforcement**: Mandatory `code_challenge` / `code_verifier` preventing authorization code interception attacks on public clients.
2. **Rotating JWKSource**: `RSAKey` generation with key rotation scheduling without service downtime.

---

## 💬 Interview Talking Points
- *Question*: "Why is PKCE (Proof Key for Code Exchange) mandatory in modern OAuth 2.1 for Single Page Apps?"
- *Answer*: "Public clients cannot securely store a `client_secret`. Without PKCE, a malicious app on a mobile device or browser extension could intercept the authorization code from the redirect URI and exchange it for tokens. PKCE forces the client to create a random `code_verifier` and hashed `code_challenge` before the request; the authorization server verifies the hash on token exchange, ensuring only the originating client can claim the access token."
