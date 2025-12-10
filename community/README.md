# 💬 Community Service

The Community Service is the **social hub** of the platform, enabling member interaction through forums, groups, and messaging features.

## 🌟 Responsibilities

- **Communication:** Manages forums, posts, and comments.
- **Moderation:** Provides endpoints for content reporting and moderation actions.
- **Groups:** Handles group creation and membership management.

## 💾 Data Schema Note

This service uses a **Schema-per-Tenant** approach.

- **Usage:** It relies on the tenant ID resolved by the Gateway to set the database `search_path` and operate within the isolated community schema for that tenant.
- **Data Stored:** All forum topics, posts, comments, moderation logs, and group metadata.

## 💻 Exposed APIs (REST + GraphQL Hybrid)

- **REST:** Post CRUD, Comment CRUD, Moderation endpoints.
- **GraphQL Use Case:** Used for complex, shapeable queries, such as fetching a thread combined with nested comments and fields from the Identity Service's user profile data in a single request.

## ⚙️ Building and Running

1.  **Dependencies:** Ensure you have Java 17+ installed.
2.  **Build:** Use the Maven Wrapper to compile the service and run tests:
    ```bash
    ./mvnw clean install
    ```
3.  **Run:** The service is packaged as a Docker image and run via `docker-compose` in the `/infra` directory.
