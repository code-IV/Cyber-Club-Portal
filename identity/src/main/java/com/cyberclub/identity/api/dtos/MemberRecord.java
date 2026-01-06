package com.cyberclub.identity.api.dtos;

import java.time.LocalDateTime;

public record MemberRecord(
    String service_name,
    String username,
    String email,
    String role,
    LocalDateTime created_at
) {}
