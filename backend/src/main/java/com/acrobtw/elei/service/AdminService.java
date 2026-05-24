package com.acrobtw.elei.service;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.dto.admin.SystemStatsDto;
import com.acrobtw.elei.repository.ActivityRepository;
import com.acrobtw.elei.repository.ExperienceLogRepository;
import com.acrobtw.elei.repository.UserRepository;

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
