package com.acrobtw.elei.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.activity.ActivityCompletionDto;
import com.acrobtw.elei.dto.activity.ActivityStatsResponse;
import com.acrobtw.elei.dto.activity.CreateActivityDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.service.ActivityService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;


    @PostMapping
    public ResponseEntity<Void> createActivity(
        @RequestBody CreateActivityDto dto,
        @AuthenticationPrincipal User user) {
        activityService.createActivity(user.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> completeActivity(
        @RequestBody ActivityCompletionDto dto,
        @AuthenticationPrincipal User user
    ) {
        activityService.addExperience(user.getId(), dto.activityId(), dto.unitsCompleted());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<ActivityStatsResponse> getActivityStats(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(activityService.getUserActivityStats(user.getId()));
    }
}
