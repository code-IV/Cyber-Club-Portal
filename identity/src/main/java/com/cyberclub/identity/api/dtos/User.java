package com.cyberclub.identity.api.dtos;

import java.time.Instant;
import java.util.UUID;

public record User(
    UUID id,
    String username,
    String email,
    String password,
    Instant createdAt
) {}
