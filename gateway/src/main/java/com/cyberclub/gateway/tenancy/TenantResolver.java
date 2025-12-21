package com.cyberclub.gateway.tenancy;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class TenantResolver {

    public String resolveTenant(HttpServletRequest request) {
        String tenant = extractFromHost(request);
        
        if (tenant == null) {
            tenant = extractFromHeader(request);
        }

        if (tenant == null) {
            throw new IllegalStateException("Cannot resolve tenant from request");
        }

        return sanitizeTenant(tenant);
    }

    // ---------------- CORE EXTRACTION ----------------

    private String extractFromHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host == null || host.isBlank()) return null;

        return parseHost(host);
    }

    private String extractFromHeader(HttpServletRequest request) {
        String tenant = request.getHeader("X-Tenant-Id");
        return (tenant == null || tenant.isBlank()) ? null : tenant;
    }

    // ---------------- HOST PARSING ----------------

    private String parseHost(String host) {
        // Remove port
        String normalized = host.split(":")[0].toLowerCase();

        // IPv4 → no tenant
        if (normalized.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
            return null;
        }

        // IPv6 like [::1]
        if (normalized.startsWith("[")) {
            return null;
        }

        if (normalized.equals("localhost")) {
            return null;
        }

        String[] parts = normalized.split("\\.");

        // tenant.example.com
        if (parts.length >= 3) {
            return parts[0]; // <-- correct tenant part
        }

        // // example.dev.local (optional policy based decision)
        // if (parts.length >= 2) {
        //     return parts[0];
        // }

        return null;
    }

    // ---------------- SANITIZATION ----------------

    private String sanitizeTenant(String tenant) {
        tenant = tenant.trim().toLowerCase();

        if (!tenant.matches("^[a-z0-9-]{1,50}$")) {
            throw new IllegalArgumentException("Invalid tenant identifier: " + tenant);
        }

        return tenant;
    }

    // ---------------- CUSTOM EXCEPTION ----------------
    public static class TenantNotResolvedException extends RuntimeException {
        public TenantNotResolvedException(String message) {
            super(message);
        }
    }
}
