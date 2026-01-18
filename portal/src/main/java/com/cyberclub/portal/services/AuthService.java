package com.cyberclub.portal.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cyberclub.portal.security.AuthPolicy;
import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.context.*;
import com.cyberclub.portal.dtos.AuthResult;

@Service
public class AuthService {
    
    private final ClientAuth clientAuth;

    public AuthService(ClientAuth clientAuth){
        this.clientAuth = clientAuth;
    }

    public AuthResult require(AuthPolicy policy){
        AuthResult user = clientAuth.checkMembership(UUID.fromString(UserContext.get()));
        policy.check(user);
        return user;
    }

}
