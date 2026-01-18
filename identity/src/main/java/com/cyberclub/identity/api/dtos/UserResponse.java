package com.cyberclub.identity.api.dtos;

public record UserResponse(
    String accessToken,
    String tokenType,
    Long expiresIn
){
    public UserResponse(String accessToken, Long expiresIn){
        this(accessToken, "Bearer", expiresIn);
    }
}