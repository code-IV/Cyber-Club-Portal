# 🆔 Identity Service API Contract

This document outlines the minimal, high-level REST contract for the Identity Service. All endpoints require appropriate security checks (e.g., internal tokens for user endpoints, admin tokens for management).

## 1. Authentication Endpoints

### POST /auth/oidc/callback

Handles the final step of the OIDC authorization flow.

- **Method:** `POST` (or `GET`, depending on OIDC implementation)
- **Query Params:** Expects `code` and `state` from the IdP redirect.
- **Function:** Exchanges the authorization code for an ID token and access token from the IdP. On success, it upserts the user into the `identity` schema.
- **Response:** Redirects to the frontend with the newly issued internal JWT (or an error code).

### POST /auth/token

Exchanges an authenticated session/credential for the internal JWT.

- **Method:** `POST`
- **Request Body:** May accept a short-lived token from a secure cookie or refresh token.
- **Function:** Validates the input and issues a long-lived, signed internal JWT containing the `user_id` and global `roles`.
- **Response:** `{ "token": "..." }`

## 2. User and Membership Endpoints

### GET /users/{id}

Retrieves the global profile for a specific user.

- **Method:** `GET`
- **Path Parameter:** `{id}` (Internal User ID)
- **Function:** Looks up the user in the shared `identity` schema.
- **Response:** `{ "id": 123, "email": "...", "name": "...", "global_roles": ["USER"] }`

### GET /users/{id}/memberships

Retrieves all tenants and associated roles the user belongs to.

- **Method:** `GET`
- **Path Parameter:** `{id}` (Internal User ID)
- **Function:** Looks up all tenant memberships for the user.
- **Response:**
  ```json
  [
    { "tenant_id": "cyberclub", "tenant_role": "ADMIN" },
    { "tenant_id": "mathclub", "tenant_role": "MEMBER" }
  ]
  ```

### PUT /users/{id}/roles

Updates global or administrative roles for a user (Requires Admin privileges).

- **Method:** `PUT`
- **Path Parameter:** `{id}` (Internal User ID)
- **Request Body:** `{ "roles": ["ADMIN", "USER"] }`
