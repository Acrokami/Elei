package com.acrobtw.elei.domain.activity;

import java.util.List;


import org.springframework.stereotype.Service;


import com.acrobtw.elei.domain.activity.dto.CreateActivityDto;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllSystemActivities() {
        return activityRepository.findAll();
    }

    public Activity createSystemActivity(CreateActivityDto dto) {
        log.info("[SYSTEM] Registering new global activity protocol: {}", dto.name());
        Activity activity = new Activity(dto.name(), dto.pointsMultiplier(), dto.measurementName());
        return activityRepository.save(activity);
    }
}
