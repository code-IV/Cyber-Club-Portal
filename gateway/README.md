# 🚪 API Gateway

The API Gateway is the **single entrypoint** for all external traffic and internal client requests. It provides essential cross-cutting concerns before routing traffic to the correct downstream service.

## 🌟 Responsibilities

- **Routing:** Routes incoming `/api/...` requests to the appropriate downstream service (Portal, Community, Learn, Challenge, etc.).
- **Authentication/Security:** Validates incoming internal JWTs (issued by the Identity Service).
- **Tenant Context Injection:** Performs **Tenant Resolution** (delegating the logic) and injects the resolved tenant ID into a dedicated HTTP header (e.g., `X-Tenant-ID`) for downstream services to use.
- **Cross-Cutting Concerns:** Centralizes CORS configuration, rate-limits, and handles TLS termination (in production environments).
- **Monitoring:** Optionally exposes `/health` and `/metrics` endpoints.

## 📝 Implementation Note

This service is intended to be a **lightweight reverse-proxy**. The current implementation can utilize a dedicated routing service built using a lightweight framework (like Spring Cloud Gateway) or a standalone reverse proxy solution.

## 💻 Exposed APIs

The Gateway primarily exposes the external API namespace and proxies requests:

- `Proxy all /api/...` routes.
- `Optional /health` and `/metrics` endpoints.

## ⚙️ Building and Running

1.  **Dependencies:** Depends on the underlying proxy technology chosen (e.g., Java/Spring Boot if Spring Cloud Gateway is used).
2.  **Build:** If built with Java, use the Maven Wrapper:
    ```bash
    ./mvnw clean install
    ```
3.  **Run:** The service is Dockerized and managed by `docker-compose` in the `/infra` directory.
