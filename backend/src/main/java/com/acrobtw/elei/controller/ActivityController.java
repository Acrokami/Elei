package com.acrobtw.elei.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.activity.ActivityCompletionDto;
import com.acrobtw.elei.dto.activity.ActivityFeedItemDto;
import com.acrobtw.elei.dto.activity.UserStatsDto;
import com.acrobtw.elei.dto.activity.CreateActivityDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.service.progression.ActivityService;

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
    public ResponseEntity<UserStatsDto> getActivityStats(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(activityService.getUserStats(user.getId()));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<ActivityFeedItemDto>> getFeed(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.getActivityFeed(user.getId()));
    }


    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void>deleteActivity(
        @PathVariable Long activityId,
        @AuthenticationPrincipal User user
    ) {
        activityService.deleteActivity(user.getId(), activityId);
        return ResponseEntity.noContent().build();
    }
}
