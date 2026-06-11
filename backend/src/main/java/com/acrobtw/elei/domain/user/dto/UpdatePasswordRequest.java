package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for updating a user's password")
public record UpdatePasswordRequest(
    @Schema(description = "The current password for the user")
    @NotBlank
    String currentPassword,

    @Schema(description = "The new password for the user", example = "securePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String newPassword
) {
}
