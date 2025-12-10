# 🛠️ Infrastructure and Operations (Infra / Ops)

This folder contains the core operational tools, configurations, and scripts necessary to deploy and manage the entire multi-service, multi-tenant application stack.

## 🌟 Responsibilities

- **Orchestration:** Defines and manages the local and staging runtime environments using `docker-compose`.
- **Schema Migration:** Stores Flyway migration scripts and orchestrates their execution during tenant provisioning and updates.
- **Dev Tools:** Provides development runbook endpoints and a simple UI/script for manual tenant provisioning/inspection by developers and operators.

## 📁 Key Files

- `docker-compose.yml`: Defines all services, networking, and dependencies (including the Supabase PostgreSQL container).
- `/flyway`: Directory containing all versioned database migration scripts (`V1__...`, `V2__...`, etc.).
- `runbook_api.sh`: Script or simple API for triggering development/operator actions (e.g., `create_tenant.sh`).

## ⚙️ Building and Running the Full Stack

This is the primary location for starting the application locally.

1.  **Prerequisites:** Ensure all services have been built (e.g., run `./mvnw clean install` in the respective Java service folders).
2.  **Launch:** Execute the main orchestration script:
    ```bash
    docker-compose up --build
    ```
3.  **Dev Tools:** Access the runbook endpoints (if implemented) for manual operational tasks.
