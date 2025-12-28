package com.cyberclub.identity.api.dtos;

public record IsMemberResponse(
    boolean allowed,
    String role
) {} 