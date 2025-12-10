# 🌐 Portal Service

The Portal Service provides the **foundation and administration** layer for each individual tenant, handling the onboarding process and tenant-level settings.

## 🌟 Responsibilities

- **Tenant Onboarding:** Manages the self-serve signup flow and initiation of new tenants.
- **Tenant Lifecycle:** Orchestrates the creation of tenant schemas upon registration.
- **Administration:** Provides the administrative user interface endpoints for tenant-level settings.
- **Content:** Manages global announcements and basic public portal content.

## 🚀 Self-Serve Tenant Creation Workflow

The Portal Service manages the critical, multi-step sequence for automatically provisioning a new tenant via the self-serve signup flow.

1.  **Validate Request:** Authenticate the incoming request and validate the proposed tenant ID (e.g., uniqueness, compliance with naming rules).
2.  **Create Schema:** Execute the database command to create the isolated schema for the new tenant: `CREATE SCHEMA tenant_x`.
3.  **Run Flyway:** Execute the **service-baseline** Flyway migration scripts against the newly created schema (`tenant_x`), setting up all necessary tables for the Community, Learn, and Challenge tenants.
4.  **Seed Defaults:** Seed the new schema with default roles, settings, and initial administrative content.
5.  **Create Membership:** Create the initial membership record for the signing-up user, assigning them the `ADMIN` role within the new tenant's context.
6.  **Notify Identity Service:** Send a notification (e.g., via webhook/API call) to the **Identity Service** to update the user's global membership table, allowing the user to be routed to the new tenant.

## 💾 Data Schema Note

This service uses a **Schema-per-Tenant** approach.

- **Usage:** It dynamically sets the database `search_path` to the tenant's schema, ensuring all data operations are strictly isolated.
- **Data Stored:** Tenant registration metadata, tenant-level settings, and announcement data.

## 💻 Exposed APIs (REST)

- `tenant registration`: Endpoints to initiate the self-serve tenant sign-up flow.
- `tenant admin endpoints`: APIs for managing tenant-specific configuration.

## ⚙️ Building and Running

1.  **Dependencies:** Ensure you have Java 17+ installed.
2.  **Build:** Use the Maven Wrapper to compile the service and run tests:
    ```bash
    ./mvnw clean install
    ```
3.  **Run:** The service is packaged as a Docker image and run via `docker-compose` in the `/infra` directory.
