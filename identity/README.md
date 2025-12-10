# 🆔 Identity Service

This service is the **global shared identity and authentication backbone** for the Cyber Club Portal. It manages all user profiles, authentication integration, and tenant membership tracking across the entire platform.

## 🌟 Responsibilities

- **Authentication:** Integration with the University Identity Provider (IdP) using OIDC/OAuth2 login flows.
- **User Management:** Map and normalize external IdP identity data to the internal `User` model. Manage global user profiles.
- **Membership:** Manage user-to-tenant memberships and associated roles.
- **Token Issuance:** Issue signed internal JWTs for other services to consume, containing the user ID and core roles.

## 💾 Data Schema Note

This service is unique as it uses a **shared global schema** named `identity` for core user data.

- **Data Stored:** Global users, tenant memberships, and roles.
- **Usage:** Other services rely on the identity data but **do not write** to this schema; they only read identity data when needed to enrich their tenant-specific data.

## 💻 Exposed APIs (REST)

| Endpoint                  | Purpose                                                               |
| :------------------------ | :-------------------------------------------------------------------- |
| `/auth/oidc/callback`     | Handles the redirect from the IdP after successful login.             |
| `/auth/token`             | Issues a signed internal JWT after successful authentication.         |
| `/users/{id}`             | Retrieves global user profile details.                                |
| `/users/{id}/memberships` | Retrieves all tenants a user belongs to.                              |
| `[Admin]`                 | Endpoints for administrative user management (e.g., role assignment). |

**➡️ Full API Contract:** The detailed contract for these endpoints is maintained in the dedicated specification file: **`specs/auth-endpoints.md`**.

## 📢 Internal Events

This service notifies other components of critical changes via internal events (e.g., REST callbacks or webhooks):

- `user.created`: When a new user is registered.
- `membership.changed`: When a user is added to or removed from a tenant.

## 🔑 Required Environment Variables

The following variables must be configured for the service to function:

| Variable             | Purpose                                                                              |
| :------------------- | :----------------------------------------------------------------------------------- |
| `OIDC_ISSUER`        | Base URL of the University's OIDC Provider.                                          |
| `OIDC_CLIENT_ID`     | Client ID registered with the OIDC Provider.                                         |
| `OIDC_CLIENT_SECRET` | Secret key for the registered OIDC Client.                                           |
| `JWT_SIGNING_KEY`    | Secret key used to sign internal JWTs for service consumption.                       |
| `POSTGRES_URL`       | Connection URL for the Supabase/PostgreSQL database (pointing to the shared schema). |

## ⚙️ Building and Running

1.  **Dependencies:** Ensure you have Java 17+ installed.
2.  **Build:** Use the Maven Wrapper to compile the service and run tests:
    ```bash
    ./mvnw clean install
    ```
3.  **Run:** The service is packaged as a Docker image and run via `docker-compose` in the `/infra` directory.
