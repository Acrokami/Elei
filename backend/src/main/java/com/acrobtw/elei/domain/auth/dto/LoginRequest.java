package com.acrobtw.elei.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username cannot be empty")
    String username,
    @NotBlank(message = "Password cannot be empty")
    String password
) {}
