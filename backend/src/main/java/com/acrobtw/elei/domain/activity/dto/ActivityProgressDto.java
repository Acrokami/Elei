package com.acrobtw.elei.domain.activity.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "User's progress details within a specific activity category")
public record ActivityProgressDto(

    @Schema(description = "Unique identifier of the activity", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long activityId,

    @Schema(description = "Name of the activity or category", example = "Reading Documentation", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String name,

    @Schema(description = "Experience multiplier for this specific category", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Integer pointsMultiplier,

    @Schema(description = "Total experience earned by the user in this category", example = "450", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Integer userCategoryExperience,

    @Schema(description = "Measurement unit for the activity", example = "pages", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String unitName
) implements Serializable {}
