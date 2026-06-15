package com.acrobtw.elei.domain.activity;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.acrobtw.elei.domain.activity.dto.ActivityCompletionDto;
import com.acrobtw.elei.domain.activity.dto.ActivityFeedItemDto;
import com.acrobtw.elei.domain.activity.dto.CreateActivityDto;
import com.acrobtw.elei.domain.experience.ExperienceService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.dto.UserStatsDto;
import com.acrobtw.elei.domain.user.service.UserStatsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@Tag(name = "Activity", description = "System activities and progress management")
public class ActivityController {

    private final ActivityService activityService;
    private final ExperienceService experienceService;
    private final UserStatsService userStatsService;

    @Operation(summary = "Get all system activities", description = "Returns default system activities available for completion")
    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllSystemActivities());
    }

    @Operation(summary = "Complete an activity", description = "Awards experience points for completing activity units")
    @PostMapping("/complete")
    public ResponseEntity<Void> completeActivity(
        @Valid @RequestBody ActivityCompletionDto dto,
        @AuthenticationPrincipal User user
    ) {
        experienceService.addExperienceFromActivity(user.getId(), dto.activityId(), dto.measurementCompleted());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user statistics", description = "Returns aggregated activity statistics for the current user")
    @GetMapping("/stats")
    public ResponseEntity<UserStatsDto> getActivityStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userStatsService.getUserProgress(user.getId()));
    }

    @Operation(summary = "Get activity feed", description = "Returns a list of recent activities to display in the feed")
    @GetMapping("/feed")
    public ResponseEntity<List<ActivityFeedItemDto>> getFeed(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(experienceService.getActivityFeed(user.getId()));
    }



    @Operation(summary = "Create system activity", description = "ADMIN ONLY: Adds a new global activity protocol")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createActivity(@Valid @RequestBody CreateActivityDto dto) {
        activityService.createSystemActivity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
