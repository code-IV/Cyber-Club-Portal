# 📚 Learn Service

The Learn Service is the **educational backbone**, managing structured content and tracking user progress through courses and modules.

## 🌟 Responsibilities

- **Content Management:** Manages the creation and organization of courses, modules, and lessons.
- **Progress Tracking:** Records user enrollment status, lesson completion, and submission history.
- **Quizzes/Submissions:** Handles lesson-related submissions and assessments.

## 💾 Data Schema Note

This service uses a **Schema-per-Tenant** approach.

- **Usage:** It relies on the tenant ID resolved by the Gateway to set the database `search_path` and operate within the isolated learn schema for that tenant.
- **Data Stored:** Course definitions, module content, enrollment records, progress tracking data, and submission results.

## 💻 Exposed APIs (REST + GraphQL Hybrid)

- **REST:** Course CRUD, Enrollment endpoints, Progress update endpoints.
- **GraphQL Use Case:** Used for efficient, combined catalog queries that fetch a list of courses, associated modules, and the current user's progress for each in a single optimized request.

## ⚙️ Building and Running

1.  **Dependencies:** Ensure you have Java 17+ installed.
2.  **Build:** Use the Maven Wrapper to compile the service and run tests:
    ```bash
    ./mvnw clean install
    ```
3.  **Run:** The service is packaged as a Docker image and run via `docker-compose` in the `/infra` directory.
