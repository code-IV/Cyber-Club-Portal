package com.cyberclub.gateway.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("jwt doFilter internal");

        try {
            String token = extractToken(request);

            String userId = extractUserId(token);
            System.out.println("gateway jwt filter: " + userId);


            // Store userId in request context or a custom UserContext
            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);
        } finally {
            // Clear context if you use a thread-local UserContext
            request.removeAttribute("userId");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new IllegalStateException("no user");
        }
        return auth.substring(7);
    }

    @SuppressWarnings("unchecked")
    private String extractUserId(String token) throws IOException {
        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            throw new IllegalStateException("malformed token");
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

        Object sub = claims.get("sub");
        if (sub == null) {
            throw new IllegalStateException("missing sub");
        }

        return sub.toString();
    }
}
