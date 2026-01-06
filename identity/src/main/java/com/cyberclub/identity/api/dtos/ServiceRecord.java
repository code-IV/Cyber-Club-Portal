package com.cyberclub.identity.api.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceRecord (
    UUID id,
    String service_name,
    LocalDateTime created_at
) {}
