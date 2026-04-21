package com.acrobtw.elei.dto;

import java.time.LocalDateTime;

public record RegisterResponse(
    int status,
    String message,
    String username,
    LocalDateTime timestamp
) {}
