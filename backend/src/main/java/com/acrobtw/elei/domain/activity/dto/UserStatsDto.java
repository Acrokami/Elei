package com.acrobtw.elei.domain.activity.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Aggregated statistics and progression data for a user")
public record UserStatsDto(

    @Schema(description = "Total accumulated experience points", example = "4500", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long totalExperience,

    @Schema(description = "Current calculated user level", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long currentLevel,

    @Schema(description = "Total experience required to reach the next level", example = "5000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long nextLevelXp,

    @Schema(description = "Breakdown of progress by individual activity categories", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    List<ActivityProgressDto> categories
) implements Serializable {}
