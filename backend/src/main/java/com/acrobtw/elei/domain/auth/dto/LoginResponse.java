package com.acrobtw.elei.domain.auth.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing the JWT token")
public record LoginResponse(
    int status,
    String username,
    String token,
    LocalDateTime timestamp
) {}
