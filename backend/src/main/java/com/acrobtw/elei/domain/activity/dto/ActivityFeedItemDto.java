package com.acrobtw.elei.domain.activity.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A single event in the user's recent activity feed")
public record ActivityFeedItemDto(
    @Schema(description = "Unique identifier of the log entry", example = "9876")
    Long logId,

    @Schema(description = "Name of the completed activity", example = "Read Spring Boot Docs")
    String activityName,

    @Schema(description = "Number of units completed during this event", example = "15")
    Integer unitsCompleted,

    @Schema(description = "Measurement unit of the activity", example = "pages")
    String unitName,

    @Schema(description = "Experience points awarded for this transaction", example = "30")
    Integer earnedXp,

    @Schema(description = "Timestamp when the activity was completed")
    LocalDateTime createdAt
) {}
