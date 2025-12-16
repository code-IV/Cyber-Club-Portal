package com.cyberclub.challenge.tenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cyberclub.challenge.context.TenantContext;

import java.io.IOException;
import java.nio.channels.IllegalSelectorException;

import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

@Component
@Order(1) // Adjust order as needed for your filter chain
public class TenantFilter extends OncePerRequestFilter  {
    
    private final TenantResolver tenantResolver;

    public TenantFilter(TenantResolver tenantResolver){
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String tenantId = tenantResolver.resolveTenant(request);
            TenantContext.set(tenantId);
        } catch (IllegalStateException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }catch (IllegalSelectorException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "tenant resolution failed");
            return;
        } finally {
            TenantContext.clear();
        }
        filterChain.doFilter(request, response);

    }

}
