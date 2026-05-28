package com.acrobtw.elei.domain.auth.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Authentication response containing the JWT token")
public record LoginResponse(
    @Schema(description = "HTTP status code", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    int status,

    @Schema(description = "Authenticated username", example = "acrobtw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String username,

    @Schema(description = "JWT Access Token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String token,

    @Schema(description = "Timestamp of login", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    LocalDateTime timestamp
) {}
