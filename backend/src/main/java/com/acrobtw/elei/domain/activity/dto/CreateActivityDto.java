package com.acrobtw.elei.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Payload for creating a new user activity")
public record CreateActivityDto(

    @Schema(description = "Name of the activity", example = "Read Spring Boot Documentation", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String name,

    @Schema(description = "Experience points multiplier for this specific activity", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(1)
    Integer pointsMultiplier,

    @Schema(description = "Measurement unit for the activity (e.g., pages, hours, tasks)", example = "pages", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String measurementName
) {}
