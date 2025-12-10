# 🌐 Portal Service

The Portal Service provides the **foundation and administration** layer for each individual tenant, handling the onboarding process and tenant-level settings.

## 🌟 Responsibilities

- **Tenant Onboarding:** Manages the self-serve signup flow and initiation of new tenants.
- **Tenant Lifecycle:** Orchestrates the creation of tenant schemas upon registration.
- **Administration:** Provides the administrative user interface endpoints for tenant-level settings.
- **Content:** Manages global announcements and basic public portal content.

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
