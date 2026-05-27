package com.acrobtw.elei.domain.auth.dto;

import java.time.LocalDateTime;

public record RegisterResponse(
    int status,
    String message,
    String username,
    String token,
    LocalDateTime timestamp
) {}
