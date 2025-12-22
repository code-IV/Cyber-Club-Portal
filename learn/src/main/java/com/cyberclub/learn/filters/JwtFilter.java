package com.cyberclub.learn.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cyberclub.learn.context.UserContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException{

        try {
            String userId = extractToken(request);
            if(userId != null){
                UserContext.setUserId(userId);
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private String extractToken(HttpServletRequest request){
        String userId = request.getHeader("X-User-Id");
        if(userId == null || userId.isBlank()){
            return null;
        }
        return userId;
    }
    
}
