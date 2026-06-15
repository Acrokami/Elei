package com.acrobtw.elei.domain.activity.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "A single event in the user's recent activity feed")
public record ActivityFeedItemDto(

    @Schema(description = "Unique identifier of the log entry", example = "9876", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long logId,

    @Schema(description = "Name of the completed activity", example = "Read Spring Boot Docs", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String activityName,

    @Schema(description = "Number of measurement completed during this event", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Integer measurementCompleted,

    @Schema(description = "Measurement unit of the activity", example = "pages", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String measurementName,

    @Schema(description = "Experience points awarded for this transaction", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Integer earnedXp,

    @Schema(description = "Timestamp when the activity was completed", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    LocalDateTime createdAt
) {}
