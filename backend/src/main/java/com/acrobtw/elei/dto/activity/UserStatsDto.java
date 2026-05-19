package com.acrobtw.elei.dto.activity;

import java.io.Serializable;
import java.util.List;

public record UserStatsDto  (
    Long totalExperience,
    Long currentLevel,
    Long nextLevelXp,
    List<CategoryProgressDto> categories
) implements Serializable {}
