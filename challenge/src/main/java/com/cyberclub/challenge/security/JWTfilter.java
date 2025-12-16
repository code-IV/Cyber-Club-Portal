package com.cyberclub.challenge.security;

import com.cyberclub.challenge.context.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class JWTfilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    )throws ServletException, IOException{

        try{
            String token = extractToken(request);
            if(token != null){
                String userId = extractUserId(token);
                UserContext.setUserId(userId);
            }

            filterChain.doFilter(request, response);
        }finally{
            UserContext.clear();
        }
    }

    private String extractToken(HttpServletRequest request){

        String auth = request.getHeader("Authorization");
        if(auth == null || !auth.startsWith("Bearer ")){
            return null;
        }

        return auth.substring(7);

    }

    @SuppressWarnings("unchecked")
    private String extractUserId(String token)throws IOException{
        String[] parts = token.split("\\.");

        if(parts.length != 3){
            throw new IllegalStateException("malformed token");
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

        Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

        Object sub = claims.get("sub");
        if(sub == null){
            throw new IllegalStateException("missing sub");
        }

        return sub.toString();
    }
    
}
