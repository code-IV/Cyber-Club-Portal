package com.cyberclub.portal.dtos;

import java.time.LocalDateTime;

public record Member(
    String service_name,
    String username,
    String email,
    String role,
    LocalDateTime created_at
){}