package com.acrobtw.elei.domain.auth.dto;

import java.time.LocalDateTime;

public record LoginResponse(
    int status,
    String username,
    String token,
    LocalDateTime timestamp
) {}
