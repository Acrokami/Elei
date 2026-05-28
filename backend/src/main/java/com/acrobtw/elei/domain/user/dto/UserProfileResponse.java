package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Basic profile information of the user")
public record UserProfileResponse(

    @Schema(description = "User's unique username", example = "acrobtw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String username,

    @Schema(description = "User's registered email address", example = "admin@elei.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    String email
) {}
