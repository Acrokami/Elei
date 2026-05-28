package com.acrobtw.elei.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(description = "Data transfer object for user registration")
public record RegisterRequest(

    @Schema(description = "Desired unique username", example = "acrobtw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String username,

    @Schema(description = "User's email address", example = "admin@elei.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email,

    @Schema(description = "Account password", example = "securePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password
) {}
