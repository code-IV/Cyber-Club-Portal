# 🛡️ Cyber Club Portal

## Project Overview

The **Cyber Club Portal** is a cutting-edge **multi-service platform** designed to serve as the central hub for cybersecurity clubs, universities, and technical communities.

Its primary purpose is to **foster education, collaboration, and competitive skill development** by providing dedicated services for learning, communication, and hands-on challenges. The microservice architecture ensures that each functional area is secure and high-performing while benefiting from a unified, integrated system.

---

## 🏛️ Service Ecosystem: Key Concepts

This portal is structured around four core services, each serving a distinct and critical function in the cyber club ecosystem.

| Service       | Purpose                                                                                                                           | Key Features                                                                                                                               |
| :------------ | :-------------------------------------------------------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| **Portal**    | The **administrative and public-facing backbone** of the platform. It handles user management and provides a unified entry point. | **Admin Dashboard**, **University-Wide Home**, **User Management**, **News and Events** feed, Public Announcements.                        |
| **Community** | The **social and collaboration hub** designed to connect members and facilitate discussions.                                      | **Forums** (threaded discussions), **Groups** (for specific interests/teams), Direct/Group **Messaging**, Member Profiles.                 |
| **Learn**     | The **structured learning environment** for skill development and knowledge acquisition.                                          | Interactive **Courses** and **Modules**, Educational Content Delivery, **Progress Tracking**, Quizzes and Assessments.                     |
| **Challenge** | The **competitive hands-on environment** for testing and refining technical skills.                                               | **Programming Contests**, Integrated **Judge** System (for code execution), **Leaderboards**, **CTFs** (Capture The Flag), **Hackathons**. |

---

## 🛠️ Tech Stack & Architecture

The platform utilizes a modern **Multiple Services** architecture built for performance, scalability, and ease of deployment.

### Core Technologies

| Component                       | Technology                 | Role                                                                                                                    |
| :------------------------------ | :------------------------- | :---------------------------------------------------------------------------------------------------------------------- |
| **Frontend (UI/UX)**            | **Next.js**                | Provides a modern, high-performance, and server-side rendered (SSR/SSG) user interface.                                 |
| **Backend (Core Logic)**        | **Java Spring Boot**       | Primary backend for complex business logic, security, and structured services.                                          |
| **Backend (API/Microservices)** | **Python FastAPI**         | Used for high-performance, asynchronous services (e.g., CTF Judge System).                                              |
| **Database & Auth**             | **Supabase**               | Provides PostgreSQL database, real-time capabilities, and managed authentication.                                       |
| **Build System**                | **Maven Wrapper (`mvnw`)** | Used for building and dependency management across the Java services.                                                   |
| **API Style**                   | **REST + GraphQL Hybrid**  | REST is used for standard data operations; GraphQL is implemented for complex, efficient data fetching where necessary. |
| **Data Access**                 | **Spring JDBC Templates**  | Used for direct, fine-grained control over database connections in Java services.                                       |

### Deployment & Environment

The entire application is containerized for consistent development and deployment across all environments.

- **Packaging/Runtime:** **Dockerized Services** managed by **Docker Compose** for local development and staging environments.
- **CI/CD Pipeline:** **GitHub Actions** are utilized to run **Automated Tests** and manage the build/deployment pipeline for all services.
- **Local Development:** Simplified via **Docker Compose**.

---

## ⚙️ Implementation Details

This section outlines the specific architectural decisions and strategies used to implement the service-oriented environment.

### Service Routing & Authentication

| Feature                          | Strategy                         | Details                                                                                                                                                |
| :------------------------------- | :------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Service Resolution**           | **API Gateway Routing**          | Requests are routed to specific services (e.g., `portal`, `learn`) via an API Gateway based on path patterns or subdomains.                            |
| **Data Isolation**               | **Dedicated Schema per Service** | Utilizes a single PostgreSQL instance but isolates data using dedicated schemas for each functional service (e.g., `portal` schema, `learn` schema).   |
| **Authentication/Authorization** | **OAuth2 + OIDC**                | Authentication uses OAuth2/OIDC standards, specifically requiring **University Email** for initial sign-up and validation.                             |
| **Security Context**             | **Identity Propagation**         | The Identity service validates the JWT, and the user's identity/roles are propagated across services via internal request headers (e.g., `X-User-Id`). |
| **Service Communication**        | **Internal API Calls**           | Services communicate via lightweight REST calls, ensuring that data sharing (e.g., checking a user's challenge progress) is handled securely.          |

### Database Strategy (Schema-Per-Service)

- **Schema Activation:** Each service is configured to use its own dedicated database schema. The database `search_path` is set to ensure a service only interacts with its own tables, preventing data leakage between modules.
- **Schema Migration:** We use **Flyway** for reliable, version-controlled database schema migrations, ensuring that updates to one service's database do not affect others.

---

### Files & Repository Structure

The project uses a **Monorepo** structure to house all services and configurations in a single repository.

- `/identity`: Identity and User Management services.
- `/gateway`: API Gateway service.
- `/portal`: Next.js Frontend and related backend services.
- `/community`: Backend services for forums, groups, and messaging.
- `/learn`: Backend services for courses, modules, and progress tracking.
- `/challenge`: High-performance FastAPI services for CTFs and the Judge system.
- `/infra`: Contains configuration files like **Docker Compose** for orchestration and shared infrastructure scripts.

---

## 🚀 Getting Started

1.  **Prerequisites:** Ensure you have **Docker** and **Docker Compose** installed.
2.  **Configuration:** Configure your environment variables for **Supabase**, database credentials, and service keys within the `/infra` directory.
3.  **Build Services:** Build the Java services using the Maven Wrapper: `./mvnw clean install` in the respective service directories (e.g., `/identity`, `/portal`).
4.  **Run Locally:** Navigate to the `/infra` directory and launch the full application stack:
    ```bash
    docker-compose up --build
    ```
