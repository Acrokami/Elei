package com.acrobtw.elei.domain.admin;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.activity.ActivityRepository;
import com.acrobtw.elei.domain.admin.dto.SystemStatsDto;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.experience.ExperienceLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;

    public SystemStatsDto getGlobalTelemetry() {
        long usersCount = userRepository.count();
        long activityCount = activityRepository.count();
        long experienceCount = experienceLogRepository.count();

        return new SystemStatsDto(usersCount, activityCount, experienceCount);
    }
}
