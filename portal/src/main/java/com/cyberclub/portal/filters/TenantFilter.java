package com.cyberclub.portal.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cyberclub.portal.context.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException{
        try{
            String tenant = resolve(request);
            TenantContext.set(tenant);
            filterChain.doFilter(request, response);
        } catch (IllegalStateException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (IllegalArgumentException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "tenant resolution failed");
            return;
        } finally {
            TenantContext.clear();
        }
    }
    
    private String resolve(HttpServletRequest request) {
        String tenant = request.getHeader("X-TenantID");

        if (tenant == null || tenant.isBlank()) {
            throw new IllegalStateException("Missing Tenant");
        }
        return tenant;
    }
}