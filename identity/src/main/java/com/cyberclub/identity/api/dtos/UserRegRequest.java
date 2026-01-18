package com.cyberclub.identity.api.dtos;

public record UserRegRequest(
    String username,
    String email,
    String password
) {}
