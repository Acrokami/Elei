package com.acrobtw.elei.domain.experience;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LevelServiceTest {

    private final LevelService levelService = new LevelService();

    @Test
    void calculateLevel_NullOrNegativeXp_ReturnsLevelOne() {
        assertEquals(1L, levelService.calculateLevel(null), "Null XP should return level 1");
        assertEquals(1L, levelService.calculateLevel(-500L), "Negative XP should return level 1");
    }

    @ParameterizedTest
    @CsvSource({
        "0, 1",
        "99, 1",
        "100, 2",
        "399, 2",
        "400, 3",
        "899, 3",
        "900, 4",
        "10000, 11"
    })
    void calculateLevel_ValidXp_ReturnsCorrectLevel(Long totalXp, Long expectedLevel) {
        assertEquals(expectedLevel, levelService.calculateLevel(totalXp));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 100",
        "2, 400",
        "3, 900",
        "10, 10000"
    })
    void calculateNextLevelUp_ValidLevel_ReturnsRequiredXp(Long currentLevel, Long expectedNextLevelXp) {
        assertEquals(expectedNextLevelXp, levelService.calculateNextLevelUp(currentLevel));
    }

    @Test
    void calculateNextLevelUp_NullLevel_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> levelService.calculateNextLevelUp(null));
    }
}
