package com.acrobtw.elei.dto.activity;

import java.time.LocalDateTime;

public record ActivityFeedItemDto(Long logId,
    String activityName,
    Integer unitsCompleted,
    String unitName,
    Integer earnedXp,
    LocalDateTime createdAt
) {}
