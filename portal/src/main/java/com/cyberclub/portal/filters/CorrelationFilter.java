package com.cyberclub.portal.filters;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cyberclub.portal.context.TraceContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException{

        String correlationId = request.getHeader("X-Correlation-Id");
        if(correlationId == null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();
        }

        TraceContext.setCorrelationId(correlationId);
        response.setHeader("X-Correlation-Id", correlationId);

        try{
            filterChain.doFilter(request, response);
        }finally{
            TraceContext.clear();
        }

    }

}