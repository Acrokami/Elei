package com.acrobtw.elei.domain.activity.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User's progress details within a specific activity category")
public record CategoryProgressDto(

    @Schema(description = "Unique identifier of the activity", example = "42")
    Long activityId,

    @Schema(description = "Name of the activity or category", example = "Reading Documentation")
    String name,

    @Schema(description = "Experience multiplier for this specific category", example = "2")
    Integer pointsMultiplier,

    @Schema(description = "Total experience earned by the user in this category", example = "450")
    Integer userCategoryExperience,

    @Schema(description = "Measurement unit for the activity", example = "pages")
    String unitName
) implements Serializable {}
