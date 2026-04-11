package com.acrobtw.elei.dto;

import java.time.LocalDateTime;

public record AuthResponse(
    int status,
    String message,
    String username,
    LocalDateTime timestamp
) {}
