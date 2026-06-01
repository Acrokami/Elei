package com.acrobtw.elei.domain.activity;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.activity.dto.CreateActivityDto;
import com.acrobtw.elei.domain.experience.ExperienceLogRepository;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;



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
}
