# 🏆 Challenge Service

The Challenge Service handles the **competitive environment**, including programming contests, CTFs, and leaderboards. This service is built using high-performance Python FastAPI.

## 🌟 Responsibilities

- **Challenge Management:** Manages problems, problem statements, and event definitions (CTFs, Hackathons).
- **Scoring:** Handles submission logic and scoring updates.
- **Judge Orchestration:** Orchestrates the execution environment for code submissions (if self-hosted) or integrates with external judges.
- **Leaderboards:** Manages and exposes real-time leaderboards.

## 💾 Data Schema Note

This service uses a **Schema-per-Tenant** approach.

- **Usage:** It relies on the tenant ID resolved by the Gateway to set the database `search_path` and operate within the isolated challenge schema for that tenant.
- **Data Stored:** Challenge definitions, user submissions, scores, and leaderboard state.

## 💻 Exposed APIs (REST + GraphQL Hybrid)

- **REST:** Challenge CRUD, Submission endpoints (high-volume), Event management.
- **GraphQL Use Case (Optional):** Used for complex leaderboard queries that require aggregation or fetching aggregated stats alongside user data.

## ⚙️ Building and Running

1.  **Dependencies:** Requires Python 3.9+ and pip for package management.
2.  **Build:** Build the service's Docker image using the provided Dockerfile.
3.  **Run:** The service is packaged as a Docker image and run via `docker-compose` in the `/infra` directory.
