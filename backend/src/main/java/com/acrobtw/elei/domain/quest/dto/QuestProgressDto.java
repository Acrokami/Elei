package com.acrobtw.elei.domain.quest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User's progress on a specific quest")
public record QuestProgressDto(
    @Schema(description = "Unique ID of the quest")
    Long questId,

    @Schema(description = "Name of the quest", example = "First Blood")
    String title,

    @Schema(description = "Detailed lore/description of the quest")
    String description,

    @Schema(description = "Current progress count", example = "1")
    Integer currentCount,

    @Schema(description = "Target count required to complete the quest", example = "5")
    Integer targetCount,

    @Schema(description = "Experience reward upon completion", example = "100")
    Integer rewardXp,

    @Schema(description = "Is the quest fully completed and reward claimed?")
    Boolean isCompleted
) {}
