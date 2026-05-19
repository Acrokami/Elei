package com.acrobtw.elei.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.dto.activity.ActivityFeedItemDto;
import com.acrobtw.elei.dto.activity.UserStatsDto;
import com.acrobtw.elei.dto.activity.CategoryProgressDto;
import com.acrobtw.elei.dto.activity.CreateActivityDto;
import com.acrobtw.elei.entity.Activity;
import com.acrobtw.elei.entity.ExperienceLog;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.ResourceNotFoundException;
import com.acrobtw.elei.kafka.LevelUpProducer;
import com.acrobtw.elei.repository.ActivityRepository;
import com.acrobtw.elei.repository.ExperienceLogRepository;
import com.acrobtw.elei.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;
    private final LevelService levelService;

    private final LevelUpProducer levelUpProducer;

    @CacheEvict(value = "userStats", key = "#userId")
    @Transactional
    public void createActivity(Long userId, CreateActivityDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Activity activity = new Activity(user, dto.name(), dto.pointsMultiplier(), dto.unitName());
        activityRepository.save(activity);
    }

    @CacheEvict(value = "userStats", key = "#userId")
    @Transactional
    public void deleteActivity(Long userId, Long activityId) {
        Activity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        if(!activity.getUser().getId().equals(userId)) {
           throw new SecurityException("Access denied. Thats not your activity");
        }

        experienceLogRepository.deleteByActivityId(activityId);
        activityRepository.delete(activity);
    }

    @CacheEvict(value = "userStats", key = "#userId")
    @Transactional
    public void addExperience(Long userId, Long activityId, Integer unitsCompleted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));



        long currentTotalXp = user.getTotalExperience() != null ? user.getTotalExperience() : 0L;
        Long oldLevel = levelService.calculateLevel(currentTotalXp);

        ExperienceLog log = new ExperienceLog(activity, unitsCompleted);
        user.addExperienceLog(log);
        activity.addExperienceLog(log);

        Long newTotalXp = currentTotalXp + log.getEarnedXp();
        user.setTotalExperience(newTotalXp);
        experienceLogRepository.save(log);

        Long newLevel = levelService.calculateLevel(newTotalXp);
        if(newLevel > oldLevel) {
            levelUpProducer.sendLevelUpEvent(userId, newLevel);
        }
    }

    @Cacheable(value = "userStats", key = "#userId")
    @Transactional
    public UserStatsDto getUserStats(Long userId) {
        System.out.println("[DB] We're going to a heavy PostgreSQL database for statistics...");
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Long totalXp = user.getTotalExperience() != null ? user.getTotalExperience() : 0;

        Long currentLevel = levelService.calculateLevel(totalXp);
        Long nextLevelUp = levelService.calculateNextLevelUp(currentLevel);


        List<Activity> userActivities = activityRepository.findByUserId(userId);

        List<CategoryProgressDto> categories = userActivities.stream()
                .map(activity -> {
                    Integer sum = experienceLogRepository.sumPointsByUserAndActivity(userId, activity.getId());
                    int safeSum = (sum != null) ? sum : 0;
                    return new CategoryProgressDto(
                            activity.getId(),
                            activity.getName(),
                            activity.getPointsMultiplier(),
                            safeSum,
                            activity.getUnitName());
                })
                .toList();

        return new UserStatsDto(
                totalXp,
                currentLevel,
                nextLevelUp,
                categories);
    }


    @Transactional
    public List<ActivityFeedItemDto> getActivityFeed(Long userId) {
        return experienceLogRepository.findTop30ByUserIdOrderByCreatedAtDesc(userId)
        .stream()
        .map(log -> new ActivityFeedItemDto(
                log.getId(),
                log.getActivity().getName(),
                log.getUnitsCompleted(),
                log.getActivity().getUnitName(),
                log.getEarnedXp(),
                log.getCreatedAt()
        ))
        .toList();
    }
}
