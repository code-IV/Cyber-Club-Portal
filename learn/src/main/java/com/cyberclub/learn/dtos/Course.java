package com.cyberclub.learn.dtos;

import java.time.Instant;
import java.util.UUID;

public record Course(
    UUID id,
    String title,
    String description,
    Instant createdAt
) {}
