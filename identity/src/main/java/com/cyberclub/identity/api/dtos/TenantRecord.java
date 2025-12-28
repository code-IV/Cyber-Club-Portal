package com.cyberclub.identity.api.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TenantRecord (
    UUID id,
    String tenantKey,
    LocalDateTime createdAt
) {}
