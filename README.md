# 🛡️ Cyber Club Portal

## Project Overview

The **Cyber Club Portal** is a cutting-edge, **multi-tenant platform** designed to serve as the central hub for cybersecurity clubs, universities, and technical communities.

Its primary purpose is to **foster education, collaboration, and competitive skill development** by providing dedicated spaces for learning, communication, and hands-on challenges. The multi-tenant architecture ensures that each club or organization maintains its own secure, isolated environment while benefiting from a unified, feature-rich system.

---

## 🏛️ Multi-Tenant Architecture: Key Concepts

This portal is structured around four core tenants (or modules), each serving a distinct and critical function in the cyber club ecosystem.

| Tenant | Purpose | Key Features |
| :--- | :--- | :--- |
| **Portal** | The **administrative and public-facing backbone** of the platform. It handles user/tenant management and provides a unified entry point. | **Admin Dashboard**, **University-Wide Home**, **Tenant Management**, **News and Events** feed, Public Announcements. |
| **Community** | The **social and collaboration hub** designed to connect members and facilitate discussions. | **Forums** (threaded discussions), **Groups** (for specific interests/teams), Direct/Group **Messaging**, Member Profiles. |
| **Learn** | The **structured learning environment** for skill development and knowledge acquisition. | Interactive **Courses** and **Modules**, Educational Content Delivery, **Progress Tracking**, Quizzes and Assessments. |
| **Challenge** | The **competitive hands-on environment** for testing and refining technical skills. | **Programming Contests**, Integrated **Judge** System (for code execution), **Leaderboards**, **CTFs** (Capture The Flag), **Hackathons**. |

---

## 🛠️ Tech Stack & Architecture

The platform utilizes a modern, decoupled service architecture built for performance, scalability, and ease of deployment.

### Core Technologies

| Component | Technology | Role |
| :--- | :--- | :--- |
| **Frontend (UI/UX)** | **Next.js** | Provides a modern, high-performance, and server-side rendered (SSR/SSG) user interface for all tenants. |
| **Backend (Core Logic)** | **Java Spring Boot** | Handles complex business logic, security, and structured backend services, particularly for the **Portal** and **Learn** tenants. |
| **Backend (API/Microservices)** | **Python FastAPI** | Used for high-performance, asynchronous services, especially suited for the intensive **Challenge** tenant (e.g., CTF scoring, judge system). |
| **Database & Auth** | **Supabase** | Acts as the primary managed backend service, providing a PostgreSQL database, real-time capabilities, and scalable authentication/authorization. |

### Deployment & Environment

The entire application is containerized for consistent development and deployment across all environments.

* **Docker:** Used to package and isolate individual backend services (Spring Boot, FastAPI) and the Next.js application.
* **Docker Compose:** Defines and runs the multi-container application stack, simplifying the setup, networking, and environment management of all services.

---

## 🚀 Getting Started

*(This section is a placeholder. You would add instructions here for installation, setup, and deployment.)*

1.  **Prerequisites:** Ensure you have **Docker** and **Docker Compose** installed.
2.  **Configuration:** Set up your environment variables (e.g., Supabase API keys, database credentials).
3.  **Run Locally:** Use `docker-compose up --build` to launch all services.

---

Would you like me to draft a more detailed "Getting Started" section, including assumed file structure and environment variable examples?
