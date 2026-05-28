package com.acrobtw.elei.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for user authentication")
public record LoginRequest(

    @Schema(description = "User's registered email or username", example = "admin@elei.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username cannot be empty")
    String username,

    @Schema(description = "Account password", example = "securePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be empty")
    String password
) {}
