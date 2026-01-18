package com.cyberclub.portal.services;

import org.springframework.stereotype.Service;

// import com.cyberclub.portal.services.AuthService;
import com.cyberclub.portal.context.*;
import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.security.Policies;

import java.util.Map;

@Service
public class InfoService{
    private final AuthService auth;

    public InfoService(AuthService auth){
        this.auth = auth;
    }

    public Map<String, String> getInfo(){
        AuthResult user = auth.require(Policies.MEMBER.or(Policies.ADMIN));

        return Map.of(
            "userId", UserContext.get(),
            "serviName", "portal",
            "role", user.role(),
            "message", "this user is member"
        );
    }
}