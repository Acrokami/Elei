package com.acrobtw.elei.domain.quest.dto;

import com.acrobtw.elei.domain.quest.enums.EventType;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Payload for quest-related asynchronous events")
public record QuestEventPayload(

    @Schema(description = "Username of the citizen who triggered the event", example = "acrobtw")
    String username,

    @Schema(description = "Type of the action performed", example = "ACTIVITY_COMPLETED")
    EventType eventType

) {}
