package com.acrobtw.elei.domain.experience;

import org.springframework.stereotype.Service;

@Service
public class LevelService {


    public Long calculateLevel(Long totalXp) {
        if (totalXp == null || totalXp < 0) return (long) 1;
        return (long) Math.sqrt(totalXp / 100.0) + 1;
    }

    public Long calculateNextLevelUp(Long currentLevel) {
        if (currentLevel == null || currentLevel < 1) {
        throw new IllegalArgumentException("System Error: Level anomaly detected. Current level cannot be null or less than 1.");
    }
        return (long) Math.pow(currentLevel,2)  * 100;
    }
}
