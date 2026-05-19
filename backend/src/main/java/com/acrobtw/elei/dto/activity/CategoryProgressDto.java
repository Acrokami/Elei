package com.acrobtw.elei.dto.activity;

import java.io.Serializable;

public record CategoryProgressDto(
    Long activityId,
    String name,
    Integer pointsMultiplier,
    Integer userCategoryExperience,
    String unitName
) implements Serializable {}
