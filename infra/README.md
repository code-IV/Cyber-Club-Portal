# 🛠️ Infrastructure and Operations (Infra / Ops)

This folder contains the core operational tools, configurations, and scripts necessary to deploy and manage the entire multi-service, multi-tenant application stack.

## 🌟 Responsibilities

- **Orchestration:** Defines and manages the local and staging runtime environments using `docker-compose`.
- **Schema Migration:** Stores Flyway migration scripts and orchestrates their execution during tenant provisioning and updates.
- **Dev Tools:** Provides development runbook endpoints and a simple UI/script for manual tenant provisioning/inspection by developers and operators.

## 💾 Flyway Migration Structure

The Flyway scripts are organized here to manage both the global identity schema and the baseline for all tenant schemas.

- `migrations/identity`: Contains versioned scripts for the **shared global** `identity` schema.
- `migrations/service-baseline`: Contains the **baseline scripts** that are run automatically by the Portal Service to set up the initial set of tables (e.g., `community`, `learn`, `challenge` schemas) whenever a **new tenant is created**.

> **Schema Migration Note:** The **Portal Service** is responsible for triggering the Flyway process to run the appropriate **service-baseline** scripts against the newly created schema whenever a new tenant is provisioned via the self-serve signup flow.

## 📁 Key Files

- `docker-compose.yml`: Defines all services, networking, and dependencies (including the Supabase PostgreSQL container).
- `runbook_api.sh`: Script or simple API for triggering development/operator actions (e.g., `create_tenant.sh`).

## ⚙️ Building and Running the Full Stack

This is the primary location for starting the application locally.

1.  **Prerequisites:** Ensure all services have been built (e.g., run `./mvnw clean install` in the respective Java service folders).
2.  **Launch:** Execute the main orchestration script:
    ```bash
    docker-compose up --build
    ```
3.  **Dev Tools:** Access the runbook endpoints (if implemented) for manual operational tasks.
