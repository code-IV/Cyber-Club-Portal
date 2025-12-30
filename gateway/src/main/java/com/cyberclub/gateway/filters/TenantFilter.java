package com.cyberclub.gateway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.channels.IllegalSelectorException;

import com.cyberclub.gateway.tenancy.TenantResolver;

@Component
@Order(1)
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
            System.out.println("gateway tenant filter === " + tenantId);
            request.setAttribute("tenantId", tenantId);
            filterChain.doFilter(request, response);
        } catch (IllegalStateException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (IllegalSelectorException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "tenant resolution failed");
            return;
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } finally {
        }

    }

}
