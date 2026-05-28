package com.acrobtw.elei.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Payload for creating a new user activity")
public record CreateActivityDto(

    @Schema(description = "Name of the activity", example = "Read Spring Boot Documentation", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "Experience points multiplier for this specific activity", example = "1")
    Integer pointsMultiplier,

    @Schema(description = "Measurement unit for the activity (e.g., pages, hours, tasks)", example = "pages", requiredMode = Schema.RequiredMode.REQUIRED)
    String unitName
) {}
