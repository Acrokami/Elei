package com.acrobtw.elei.service.progression;

import org.springframework.stereotype.Service;

@Service
public class LevelService {


    public Long calculateLevel(Long totalXp) {
        if (totalXp == null || totalXp < 0) return (long) 1;
        return (long) Math.sqrt(totalXp / 100.0) + 1;
    }

    public Long calculateNextLevelUp(Long currentLevel) {
        return (long) Math.pow(currentLevel,2)  * 100;
    }
}
