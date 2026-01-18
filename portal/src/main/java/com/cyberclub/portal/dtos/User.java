package com.cyberclub.portal.dtos;

import java.util.UUID;

public record User(
    UUID id,
    String username,
    String email,
    String role
) {}
