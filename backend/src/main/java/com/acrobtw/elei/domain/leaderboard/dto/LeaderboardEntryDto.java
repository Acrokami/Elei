package com.acrobtw.elei.domain.leaderboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Single entry in the global leaderboard")
public record LeaderboardEntryDto(

    @Schema(description = "Unique user identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long id,

    @Schema(description = "User's display name", example = "acrobtw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String username,

    @Schema(description = "Total accumulated experience points", example = "15420", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long score
) {}
