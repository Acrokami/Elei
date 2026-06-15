package com.acrobtw.elei.domain.user.dto;


import java.util.List;

import com.acrobtw.elei.domain.auth.enums.AuthProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Basic profile information of the user")
public record UserProfileResponse(

    @Schema(description = "User's unique username", example = "acrobtw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String username,

    @Schema(description = "User's registered email address", example = "admin@elei.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    String email,

    @Schema(description = "User's provider", example = "GITHUB", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    AuthProvider provider,

    @Schema(description = "List of dates the user was active in YYY-MM-DD format", example = "[\"2026-05-30\", \"2026-05-31\"]", requiredMode =  Schema.RequiredMode.REQUIRED)
    List<String> activeDays,

    @Schema(description = "User's current level", example = "2", requiredMode =  Schema.RequiredMode.REQUIRED)
    @NotNull
    Integer level,

    @Schema(description = "Total accumulated experience", example = "150", requiredMode =  Schema.RequiredMode.REQUIRED)
    @NotNull
    Long totalExperience,

    @Schema(description = "Experience required for the next level", example = "300", requiredMode =  Schema.RequiredMode.REQUIRED)
    @NotNull
    Long nextLevelExperience,

    @Schema(description = "Citizen rank title", example = "Novice", requiredMode =  Schema.RequiredMode.REQUIRED)
    @NotBlank
    String rank,

    @Schema(description = "User role in the system", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String role
) {}
