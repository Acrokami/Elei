package com.acrobtw.elei.dto.auth;

import java.time.LocalDateTime;

public record RegisterResponse(
    int status,
    String message,
    String username,
    String token,
    LocalDateTime timestamp
) {}
