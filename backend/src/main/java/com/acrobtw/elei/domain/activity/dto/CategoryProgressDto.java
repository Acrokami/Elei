package com.acrobtw.elei.domain.activity.dto;

import java.io.Serializable;

public record CategoryProgressDto(
    Long activityId,
    String name,
    Integer pointsMultiplier,
    Integer userCategoryExperience,
    String unitName
) implements Serializable {}
