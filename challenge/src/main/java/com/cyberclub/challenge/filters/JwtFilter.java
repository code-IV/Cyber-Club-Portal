package com.cyberclub.challenge.filters;

import com.cyberclub.challenge.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    )throws ServletException, IOException{

        try{
            UUID userId = extractToken(request);
            if(userId != null){
                UserContext.setId(userId);
            }

            filterChain.doFilter(request, response);
        }finally{
            UserContext.clear();
        }
    }

    private UUID extractToken(HttpServletRequest request){

        String auth = request.getHeader("X-User-Id");
        
        if(auth == null || auth.isBlank()){
            return null;
        }

        return UUID.fromString(auth);

    }
    
}
