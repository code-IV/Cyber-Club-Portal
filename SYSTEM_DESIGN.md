# System Design & Architectural Overview: Cyber Club Platform

## 1. Design Philosophy

The Cyber Club Platform is engineered as a modular, multi-service ecosystem. The primary architectural objective was to resolve the "Distributed Monolith" trap by enforcing strict service boundaries, data isolation, and centralized identity propagation.

This system is built for **evolutionary architecture**—allowing individual domains (Learn, Challenge, Community) to scale or be rewritten independently without impacting the global state.

## 2. Architectural Patterns

### Service-Oriented Isolation

Unlike traditional tiered architectures, this platform utilizes **Schema-per-Service** isolation.

- **Ownership:** Each domain (Portal, Learn, Challenge, Community) owns its logic and its data.
- **Communication:** Cross-service data requirements are satisfied via internal API contracts rather than shared database state, preventing "spaghetti joins" and deadlocks.

### Centralized Identity & Role Propagation

To reduce the cognitive load on individual services, authentication is handled centrally.

- **Identity Provider:** Managed via the Identity Service.
- **Propagation:** The API Gateway validates ingress JWTs and injects an `X-User-Id` and `X-User-Role` into the internal request headers.
- **Authorization:** Services remain stateless regarding session management, performing only local authorization checks based on propagated headers.

### Gateway-Mediated Routing

The API Gateway acts as the system's "Front Controller," managing:

- **Protocol Translation:** Standardizing external requests into internal service calls.
- **Security Scoping:** Preventing unauthorized internal service exposure.
- **Request Correlation:** Injecting `X-Request-Id` for distributed tracing across services.

## 3. Data Integrity & Migration Strategy

Consistency is managed through a decentralized migration pattern:

- **Flyway Integration:** Each service maintains its own migration history.
- **Schema Activation:** Services utilize specific PostgreSQL schemas, managed via `search_path` configuration, to ensure a single database instance provides multi-service isolation without the overhead of multiple RDS instances.

## 4. Engineering Trade-offs

- **Complexity vs. Autonomy:** The decision to move to a multi-service model introduced higher initial setup complexity (distributed logging, service discovery). This was accepted to gain **Deployment Autonomy**, allowing the `Challenge` service (Python/FastAPI) to iterate faster than the `Portal` service (Java/Spring).
- **Consistency Model:** The system favors **Eventual Consistency** for cross-service updates (e.g., updating a user's profile across Community and Learn) to maintain high availability.

## 5. Future Scalability & Evolution

- **Horizontal Scaling:** Because services are stateless and schema-isolated, specific high-traffic modules can scale their application tier independently, though database scaling remains shared.
- **Extensibility:** The architecture supports "Plug-and-Play" domains. Adding a new `Research` or `JobBoard` service requires zero modification to the existing core logic.

---

_This document serves as the architectural defense for the Cyber Club Platform design decisions._
