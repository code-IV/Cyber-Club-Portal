package com.cyberclub.gateway.tenancy;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class ServiceResolver {

    public String resolveService(HttpServletRequest request) {
        String serviceName = extractFromHost(request);
        
        if (serviceName == null) {
            serviceName = extractFromHeader(request);
        }

        if (serviceName == null) {
            return "portal";
        }

        return sanitizeService(serviceName);
    }

    // ---------------- CORE EXTRACTION ----------------

    private String extractFromHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host == null || host.isBlank()) return null;

        return parseHost(host);
    }

    private String extractFromHeader(HttpServletRequest request) {
        String serviceName = request.getHeader("X-Service-Name");
        return (serviceName == null || serviceName.isBlank()) ? null : serviceName;
    }

    // ---------------- HOST PARSING ----------------

    private String parseHost(String host) {
        // Remove port
        String normalized = host.split(":")[0].toLowerCase();

        // IPv4 → no service
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

        // service.example.com
        if (parts.length >= 3) {
            return parts[0]; // <-- correct service part
        }

        // // example.dev.local (optional policy based decision)
        // if (parts.length >= 2) {
        //     return parts[0];
        // }

        return null;
    }

    // ---------------- SANITIZATION ----------------

    private String sanitizeService(String serviceName) {
        serviceName = serviceName.trim().toLowerCase();

        if (!serviceName.matches("^[a-z0-9]([a-z0-9-]{0,48}[a-z0-9])?$")) {
            throw new IllegalArgumentException("Invalid service identifier format");
        }

        return serviceName;
    }

    // ---------------- CUSTOM EXCEPTION ----------------
    public static class ServiceNotResolvedException extends RuntimeException {
        public ServiceNotResolvedException(String message) {
            super(message);
        }
    }
}
