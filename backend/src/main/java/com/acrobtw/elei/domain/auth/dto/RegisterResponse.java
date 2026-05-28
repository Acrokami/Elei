package com.acrobtw.elei.domain.auth.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration response with JWT token and status details")
public record RegisterResponse(

    @Schema(description = "HTTP status code or internal operation status", example = "200")
    int status,

    @Schema(description = "Informational message regarding the registration result", example = "User successfully registered")
    String message,

    @Schema(description = "The registered username", example = "acrobtw")
    String username,

    @Schema(description = "JWT access token for immediate API authorization", example = "eyJhbGciOiJIUzI1NiIsInR5c...")
    String token,

    @Schema(description = "Timestamp of the successful registration process")
    LocalDateTime timestamp
) {}
