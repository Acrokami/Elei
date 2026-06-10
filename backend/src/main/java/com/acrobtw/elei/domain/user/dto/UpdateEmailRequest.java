package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for updating a user's email address")
public record UpdateEmailRequest(
    @Schema(description = "The new email address for the user")
    String newEmail
) { 
}
