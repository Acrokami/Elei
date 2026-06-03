package com.acrobtw.elei.domain.experience;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.activity.Activity;
import com.acrobtw.elei.domain.activity.ActivityRepository;
import com.acrobtw.elei.domain.activity.dto.ActivityFeedItemDto;
import com.acrobtw.elei.domain.leaderboard.LeaderboardService;
import com.acrobtw.elei.domain.notification.NotificationProducer;
import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.service.QuestEventProducer;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;
    private final LevelService levelService;
    private final LeaderboardService leaderboardService;
    private final NotificationProducer notificationDispatchService;
    private final QuestEventProducer questEventProducer;

    @CacheEvict(value = "userStats", key = "#userId")
    @Transactional
    public void addExperienceFromActivity(Long userId, Long activityId, Integer unitsCompleted) {
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

        leaderboardService.updateScore(user.getId(), user.getUsername(), newTotalXp);

        checkAndDispatchLevelUp(user, oldLevel, newTotalXp);
        questEventProducer.sendEvent(user.getUsername(), EventType.ACTIVITY_COMPLETED);
    }


    @Transactional
    public void addRawExperience(String username, Long bonusXp) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));

        long currentTotalXp = user.getTotalExperience() != null ? user.getTotalExperience() : 0L;
        long oldLevel = levelService.calculateLevel(currentTotalXp);

        Long newTotalXp = currentTotalXp + bonusXp;
        user.setTotalExperience(newTotalXp);
        userRepository.save(user);
        leaderboardService.updateScore(user.getId(), user.getUsername(), newTotalXp);

        checkAndDispatchLevelUp(user, oldLevel, newTotalXp);
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


    private void checkAndDispatchLevelUp(User user, Long oldLevel, Long newTotalXp) {
        Long newLevel = levelService.calculateLevel(newTotalXp);
        if (newLevel > oldLevel) {
            notificationDispatchService.sendLevelUpEvent(user.getUsername(), newLevel);
        }
}
}
