package com.cyberclub.identity.api.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRecord (
    UUID id,
    String username,
    String email,
    LocalDateTime createdAt
) {}
