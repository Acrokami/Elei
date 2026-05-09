package com.acrobtw.elei.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.dto.activity.ActivityFeedItemDto;
import com.acrobtw.elei.dto.activity.ActivityStatsResponse;
import com.acrobtw.elei.dto.activity.CategoryProgressDto;
import com.acrobtw.elei.dto.activity.CreateActivityDto;
import com.acrobtw.elei.entity.Activity;
import com.acrobtw.elei.entity.ExperienceLog;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.ResourceNotFoundException;
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




    @Transactional
    public void createActivity(Long userId, CreateActivityDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Activity activity = new Activity(user, dto.name(), dto.pointsMultiplier(), dto.unitName());
        activityRepository.save(activity);
    }

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

    @Transactional
    public void addExperience(Long userId, Long activityId, Integer unitsCompleted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        ExperienceLog log = new ExperienceLog(activity, unitsCompleted);
        user.addExperienceLog(log);
        activity.addExperienceLog(log);

        experienceLogRepository.save(log);
    }

    @Transactional
    public ActivityStatsResponse getUserActivityStats(Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

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

        return new ActivityStatsResponse(
                user.getTotalExperience(),
                user.getLevel(),
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
