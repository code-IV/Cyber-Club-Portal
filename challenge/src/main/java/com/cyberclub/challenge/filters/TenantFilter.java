package com.cyberclub.challenge.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import com.cyberclub.challenge.context.TenantContext;
import com.cyberclub.challenge.context.UserContext;
import com.cyberclub.challenge.security.ClientIdentity;

import java.io.IOException;
import java.util.UUID;

// import org.springframework.core.annotation.Order;

@Component
public class TenantFilter extends OncePerRequestFilter  {

    private final ClientIdentity clientIdentity;

    public TenantFilter(ClientIdentity clientIdentity){
        this.clientIdentity = clientIdentity;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String tenantKey = resolve(request);
            UUID userId = UserContext.getId();

            if(userId == null){
                ((HttpServletResponse) response).setStatus(401);
                return;
            }

            var result = clientIdentity.checkMembership(userId, tenantKey);

            if(result == null || !result.allowed){
                ((HttpServletResponse) response).setStatus(403);
                return;
            }

            // UserContext.setRole(result.role);
            TenantContext.set(tenantKey);
            filterChain.doFilter(request, response);
        } catch (IllegalStateException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }catch (IllegalArgumentException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "tenant resolution failed");
            return;
        } finally {
            TenantContext.clear();
        }

    }

    private String resolve(HttpServletRequest request){
        String tenant = request.getHeader("X-Tenant-Id");
        if(tenant == null || tenant.isBlank()){
            throw new IllegalStateException("Missing Tenant");
        }
        return tenant;
    }

}
