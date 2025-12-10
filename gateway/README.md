# 🚪 API Gateway

The API Gateway is the **single, mandatory entrypoint** for all external traffic destined for the Cyber Club Portal services. It ensures consistent security, tenancy, and routing across the platform.

## 🌟 Responsibilities

The Gateway handles several critical cross-cutting concerns:

1.  **Routing (Entry Point):** Proxies all incoming `/api/...` requests to the correct downstream service (Identity, Portal, Community, etc.).
2.  **JWT Validation:** Validates the signature and expiry of all incoming internal JWTs issued by the Identity Service.
3.  **Tenant Resolution:** Determines the active tenant ID for the request based on a two-tiered strategy:
    - **Tier 1 (Primary):** Attempts to resolve the tenant ID from the **subdomain** in the request URL (e.g., `cyberclub.portal.com`).
    - **Tier 2 (Fallback):** If Tier 1 fails, falls back to checking the **`X-Tenant-Id`** HTTP header.

## 📝 Header Injection Rules

The Gateway is responsible for setting and standardizing critical headers before forwarding the request to a downstream service.

| Header          | Gateway Action    | Purpose                                                                                                                                                       |
| :-------------- | :---------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `X-Tenant-Id`   | **Overwrite/Set** | Injects the resolved tenant ID from the resolution process, guaranteeing all downstream services receive this context.                                        |
| `X-Request-Id`  | **Set**           | Generates and injects a unique ID for request tracing and logging across all microservices.                                                                   |
| `Authorization` | **Forward**       | Passes the original `Authorization: Bearer [JWT]` token to the downstream service for potential secondary validation (e.g., by the service's security layer). |

## 📐 Implementation Note

The Gateway must be a lightweight, high-performance reverse-proxy capable of executing Java/Spring Boot code (or Python/Go logic) to perform the mandatory **JWT validation** and **tenant resolution/injection** logic.

- **Decision (TBD):** We will decide later whether to use a Java-based solution (e.g., **Spring Cloud Gateway**) or a non-Java proxy solution (e.g., Nginx + Lua/sidecar, or a dedicated Go/Python proxy). The primary requirement remains: **All requests must be JWT-validated and tagged with the resolved `X-Tenant-Id` before routing.**

## 💻 Exposed APIs

The Gateway primarily exposes the external API namespace and proxies requests:

- `Proxy all /api/...` routes.
- `Optional /health` and `/metrics` endpoints.

## ⚙️ Building and Running

1.  **Build:** If built with Java, use the Maven Wrapper:
    ```bash
    ./mvnw clean install
    ```
2.  **Run:** The service is Dockerized and managed by `docker-compose` in the `/infra` directory.
