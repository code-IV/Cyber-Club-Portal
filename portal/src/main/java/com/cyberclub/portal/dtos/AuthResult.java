package com.cyberclub.portal.dtos;

public record AuthResult(
    boolean allowed,
    String role
) {}
