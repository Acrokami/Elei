package com.acrobtw.elei.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload for real-time WebSocket notifications")
public record NotificationPayload(

    @Schema(description = "Username of the target citizen", example = "acrobtw")
    String username,

    @Schema(description = "Notification message to display", example = "has reached Level 5!")
    String message,

    @Schema(description = "Type of the notification event", example = "LEVEL_UP")
    String eventType

) {}
