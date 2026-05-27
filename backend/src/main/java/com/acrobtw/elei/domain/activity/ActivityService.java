package com.acrobtw.elei.domain.activity;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.activity.dto.ActivityFeedItemDto;
import com.acrobtw.elei.domain.activity.dto.CategoryProgressDto;
import com.acrobtw.elei.domain.activity.dto.CreateActivityDto;
import com.acrobtw.elei.domain.activity.dto.UserStatsDto;
import com.acrobtw.elei.domain.leaderboard.LeaderboardService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.experience.ExperienceLog;
import com.acrobtw.elei.domain.user.experience.ExperienceLogRepository;
import com.acrobtw.elei.domain.user.experience.LevelService;
import com.acrobtw.elei.kafka.LevelUpProducer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;

    private final LevelService levelService;
    private final LeaderboardService leaderboardService;

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

        leaderboardService.updateScore(userId, newTotalXp);

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
