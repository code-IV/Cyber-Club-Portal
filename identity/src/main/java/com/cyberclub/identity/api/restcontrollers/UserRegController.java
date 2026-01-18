package com.cyberclub.identity.api.restcontrollers;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cyberclub.identity.api.dtos.User;
import com.cyberclub.identity.api.dtos.UserResponse;
import com.cyberclub.identity.api.dtos.UserRegRequest;
import com.cyberclub.identity.api.dtos.UserLoginRequest;
import com.cyberclub.identity.services.UserRegService;
import com.cyberclub.identity.services.JwtTokenService;
import com.cyberclub.identity.config.JwtProperties;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserRegController {

    private final UserRegService regService;
    private final JwtTokenService tokenService;
    private final JwtProperties properties;

    public UserRegController(UserRegService regService, JwtTokenService tokenService, JwtProperties properties){
        this.regService = regService;
        this.tokenService = tokenService;
        this.properties = properties;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRegRequest req) {
        User user = regService.register(
            req.username(), 
            req.email(),
            req.password()
        );
        String token = tokenService.generate(user);

        return new UserResponse(
            token,
            properties.expiration()
        );
    }

    @PostMapping("/signin")
    public UserResponse login(@RequestBody UserLoginRequest req) {
        User user = regService.authenticate(
            req.email(),
            req.password()
        );

        String token = tokenService.generate(user);

        return new UserResponse(
            token,
            properties.expiration()
        );
    }

}
