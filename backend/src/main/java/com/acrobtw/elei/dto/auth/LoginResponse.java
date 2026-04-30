package com.acrobtw.elei.dto.auth;

import java.time.LocalDateTime;

public record LoginResponse(
    int status,
    String username,
    String token,
    LocalDateTime timestamp
) {}
