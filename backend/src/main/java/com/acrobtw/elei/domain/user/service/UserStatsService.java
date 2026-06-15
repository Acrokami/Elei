package com.acrobtw.elei.domain.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.activity.Activity;
import com.acrobtw.elei.domain.activity.ActivityRepository;
import com.acrobtw.elei.domain.activity.dto.ActivityProgressDto;
import com.acrobtw.elei.domain.experience.ExperienceLogRepository;
import com.acrobtw.elei.domain.experience.LevelService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.dto.UserStatsDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatsService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;
    private final LevelService levelService;

    @Cacheable(value = "userStats", key = "#userId")
    @Transactional
    public UserStatsDto getUserProgress(Long userId) {
        log.info("[DB] Fetching heavy PostgreSQL statistics for user ID: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Long totalXp = user.getTotalExperience() != null ? user.getTotalExperience() : 0L;
        Long currentLevel = levelService.calculateLevel(totalXp);
        Long nextLevelUp = levelService.calculateNextLevelUp(currentLevel);


        List<Activity> systemActivities = activityRepository.findAll();

        List<Object[]> groupedXp = experienceLogRepository.sumAllPointsByUserGroupedByActivity(userId);

        Map<Long, Integer> xpByActivityMap = groupedXp.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).intValue()
                ));

        List<ActivityProgressDto> categories = systemActivities.stream()
                .map(activity -> {
                    int sum = xpByActivityMap.getOrDefault(activity.getId(), 0);
                    return new ActivityProgressDto(
                            activity.getId(),
                            activity.getName(),
                            activity.getPointsMultiplier(),
                            sum,
                            activity.getMeasurementUnit() 
                    );
                })
                .toList();

        return new UserStatsDto(
                totalXp,
                currentLevel,
                nextLevelUp,
                categories
        );
    }
}
