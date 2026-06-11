package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for updating a user's email address")
public record UpdateEmailRequest(
    @Schema(description = "The new email address for the user", example = "admin@elei.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email(message = "Invalid email format")
    String newEmail
) {
}
