package com.acrobtw.elei.dto.activity;

public record CategoryProgressDto(
    Long activityId,
    String name,
    Integer baseExperience,
    Integer userCategoryExperience
) {}
