package com.acrobtw.elei.domain.leaderboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Single entry in the global leaderboard")
public record LeaderboardEntryDto(
    @Schema(description = "Unique user identifier", example = "1")
    Long id,

    @Schema(description = "User's display name", example = "acrobtw")
    String username,
    
    @Schema(description = "Total accumulated experience points", example = "15420")
    Long score
) {}
