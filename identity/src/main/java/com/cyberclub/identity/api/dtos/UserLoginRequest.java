package com.cyberclub.identity.api.dtos;

public record UserLoginRequest(
    String email,
    String password
) {}
