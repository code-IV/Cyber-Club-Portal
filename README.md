# 🛡️ Cyber Club Portal

## Project Overview

The **Cyber Club Portal** is a cutting-edge, **multi-tenant platform** designed to serve as the central hub for cybersecurity clubs, universities, and technical communities.

Its primary purpose is to **foster education, collaboration, and competitive skill development** by providing dedicated spaces for learning, communication, and hands-on challenges. The multi-tenant architecture ensures that each club or organization maintains its own secure, isolated environment while benefiting from a unified, feature-rich system.

---

## 🏛️ Multi-Tenant Architecture: Key Concepts

This portal is structured around four core tenants (or modules), each serving a distinct and critical function in the cyber club ecosystem.

| Tenant        | Purpose                                                                                                                                  | Key Features                                                                                                                               |
| :------------ | :--------------------------------------------------------------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| **Portal**    | The **administrative and public-facing backbone** of the platform. It handles user/tenant management and provides a unified entry point. | **Admin Dashboard**, **University-Wide Home**, **Tenant Management**, **News and Events** feed, Public Announcements.                      |
| **Community** | The **social and collaboration hub** designed to connect members and facilitate discussions.                                             | **Forums** (threaded discussions), **Groups** (for specific interests/teams), Direct/Group **Messaging**, Member Profiles.                 |
| **Learn**     | The **structured learning environment** for skill development and knowledge acquisition.                                                 | Interactive **Courses** and **Modules**, Educational Content Delivery, **Progress Tracking**, Quizzes and Assessments.                     |
| **Challenge** | The **competitive hands-on environment** for testing and refining technical skills.                                                      | **Programming Contests**, Integrated **Judge** System (for code execution), **Leaderboards**, **CTFs** (Capture The Flag), **Hackathons**. |

---

## 🛠️ Tech Stack & Architecture

The platform utilizes a modern, **Multiple Services (Microservices)** architecture built for performance, scalability, and ease of deployment.

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

This section outlines the specific architectural decisions and strategies used to implement the multi-tenant environment.

### Tenancy Resolution & Authentication

| Feature                          | Strategy                                         | Details                                                                                                                                                                            |
| :------------------------------- | :----------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Tenant Resolution**            | **Subdomain & Request Header**                   | Tenants are resolved either via a specific subdomain (e.g., `cyberclub.portal`, `cyberclub.learn`) or via the `X-Tenant-ID` HTTP Request Header.                                   |
| **Multi-Tenancy Approach**       | **Shared DB, Separate Schema per Tenant**        | Utilizes a single PostgreSQL instance but isolates data using dedicated schemas for each tenant.                                                                                   |
| **Authentication/Authorization** | **OAuth2 + OIDC**                                | Authentication uses OAuth2/OIDC standards, specifically requiring **University Email** for initial sign-up and validation.                                                         |
| **Authentication/Token Flow**    | **Separate Context Resolution**                  | The tenant context is resolved separately (from subdomain/header) and then validated against the incoming JWT (JSON Web Token) to ensure the user belongs to the requested tenant. |
| **Tenancy Isolation**            | **Soft Isolation with Explicit Content Sharing** | Employs soft isolation, allowing for **explicit content sharing** (e.g., universal news feeds, shared challenge templates) where permitted by security rules.                      |
| **Tenant Provisioning**          | **Self-Serve Flow**                              | New tenants are provisioned via a dedicated self-serve signup process exposed by the **Portal** tenant.                                                                            |

### Database Strategy (Schema-Per-Tenant)

- **Schema Activation:** The specific tenant's schema is activated by setting the database `search_path` per connection or session immediately upon request resolution. This ensures all database operations for the request are confined to the correct schema.
- **Schema Migration:** We use **Flyway** for reliable, version-controlled database schema migrations and to manage the provisioning of new schemas during the tenant signup process.

---

### Files & Repository Structure

The project uses a **Monorepo** structure to house all services and configurations in a single repository for streamlined development and deployment.

- `/identity`: Identity and User Management services.
- `/gateway`: API Gateway service.
- `/portal`: Next.js Frontend and related backend services.
- `/community`: Backend services for forums, groups, and messaging.
- `/learn`: Backend services for courses, modules, and progress tracking.
- `/challenge`: High-performance FastAPI services for CTFs and the Judge system.
- `/infra`: Contains configuration files like **Docker Compose** for orchestration.

---

## 🚀 Getting Started

_(This section provides detailed instructions for local development.)_

1.  **Prerequisites:** Ensure you have **Docker** and **Docker Compose** installed.
2.  **Configuration:** Configure your environment variables for **Supabase**, database credentials, and tenant keys within the `/infra` directory.
3.  **Build Services:** Build the Java services using the Maven Wrapper: `./mvnw clean install` in the respective service directories (e.g., `/identity`, `/portal`).
4.  **Run Locally:** Navigate to the `/infra` directory and use the orchestration tool to launch the full application stack:
    ```bash
    docker-compose up --build
    ```

---
