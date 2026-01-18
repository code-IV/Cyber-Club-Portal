package com.cyberclub.portal.dtos;

import java.time.Instant;

public record ErrorResponse(
    String  error,
    String  message,
    String  path,
    String CorrelationId,
    Instant timestamp
) {}
