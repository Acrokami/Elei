package com.acrobtw.elei.domain.activity.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregated statistics and progression data for a user")
public record UserStatsDto(
    @Schema(description = "Total accumulated experience points", example = "4500")
    Long totalExperience,

    @Schema(description = "Current calculated user level", example = "12")
    Long currentLevel,

    @Schema(description = "Total experience required to reach the next level", example = "5000")
    Long nextLevelXp,

    @Schema(description = "Breakdown of progress by individual activity categories")
    List<CategoryProgressDto> categories
) implements Serializable {}
