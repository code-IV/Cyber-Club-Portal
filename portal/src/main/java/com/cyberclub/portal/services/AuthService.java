package com.cyberclub.portal.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.context.*;

@Service
public class AuthService {
    
    private final ClientAuth clientAuth;

    public AuthService(ClientAuth clientAuth){
        this.clientAuth = clientAuth;
    }

    public AuthResult requireMember(){
        String serviceName = "portal";
        UUID userId;
        try {
            userId = UUID.fromString(UserContext.get());
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid user context");
        }

        AuthResult result = clientAuth.checkMembership(userId, serviceName);

        if(result == null || !result.allowed()){
            throw new ForbiddenException("User is not portal member");
        }

        return result;

    }

    public AuthResult requireAdmin(){
        UUID userId;
        try {
            userId = UUID.fromString(UserContext.get());
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid user context");
        }
        
        AuthResult result = clientAuth.checkMembership(userId, "portal");

        if(result == null || !result.allowed() || !"ADMIN".equals(result.role())){
             throw new ForbiddenException("Require Admin Access");
         }

        return result;
    }

}
