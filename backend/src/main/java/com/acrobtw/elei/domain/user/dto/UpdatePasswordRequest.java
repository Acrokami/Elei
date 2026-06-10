package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for updating a user's password")
public record UpdatePasswordRequest(
    @Schema(description = "The current password for the user")
    String currentPassword,
    @Schema(description = "The new password for the user")
    String newPassword
) {
}
