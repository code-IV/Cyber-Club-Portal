package com.cyberclub.gateway.filters;

import com.cyberclub.gateway.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

@Component
@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final SecretKey key;
    private final JwtProperties properties;

    public JwtFilter(JwtProperties properties){
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = extractToken(request);

            Claims claims = validateClaims(token);

            // Store userId in request context or a custom UserContext
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("email", claims.get("email", String.class));

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } finally {
            // Clear context if you use a thread-local UserContext
            request.removeAttribute("userId");
        }
    }

    public Claims validateClaims(String token){
        return Jwts.parserBuilder()
                .requireIssuer(properties.issuer())
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new IllegalStateException("absent token");
        }
        return auth.substring(7);
    }
}
