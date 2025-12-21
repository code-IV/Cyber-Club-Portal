package com.cyberclub.gateway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


@Component
@Order(3) // Runs after JWT + Tenant filters
public class RequestForwardingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Extract tenant
            String tenantId = (String) request.getAttribute("tenantId");

            // Extract user from request attribute set by JWTfilter
            String userId = (String) request.getAttribute("userId");

            // Generate request id
            String requestId = UUID.randomUUID().toString();

            // Add headers to request for downstream (wrap the request)
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("X-Tenant-Id".equalsIgnoreCase(name)) {
                        return tenantId;
                    }
                    if ("X-User-Id".equalsIgnoreCase(name)) {
                        return userId;
                    }
                    if ("X-Request-Id".equalsIgnoreCase(name)) {
                        return requestId;
                    }
                    return super.getHeader(name);
                }
            };

            filterChain.doFilter(wrappedRequest, response);

        } catch (IllegalStateException | IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            return;
        }
    }
}
