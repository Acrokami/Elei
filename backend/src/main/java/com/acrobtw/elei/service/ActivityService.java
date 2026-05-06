package com.acrobtw.elei.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.dto.activity.ActivityStatsResponse;
import com.acrobtw.elei.dto.activity.CategoryProgressDto;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        List<Activity> allActivities = activityRepository.findAll();
        List<CategoryProgressDto> categories = allActivities.stream()
                .map(activity -> {
                    Integer sum = experienceLogRepository.sumPointsByUserAndActivity(userId, activity.getId());
                    return new CategoryProgressDto(
                            activity.getId(),
                            activity.getName(),
                            activity.getPointsMultiplier(),
                            sum);
                })
                .toList();

        return new ActivityStatsResponse(
                user.getTotalExperience(),
                user.getLevel(),
                categories);
    }
}
