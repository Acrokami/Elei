package com.acrobtw.elei.dto.activity;

import java.util.List;

public record ActivityStatsResponse(
    Long totalExperience,
    int currentLevel,
    List<CategoryProgressDto> categories
) {}
