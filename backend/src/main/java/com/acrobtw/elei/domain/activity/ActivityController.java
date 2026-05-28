package com.acrobtw.elei.domain.activity;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.domain.activity.dto.ActivityCompletionDto;
import com.acrobtw.elei.domain.activity.dto.ActivityFeedItemDto;
import com.acrobtw.elei.domain.activity.dto.CreateActivityDto;
import com.acrobtw.elei.domain.activity.dto.UserStatsDto;
import com.acrobtw.elei.domain.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@Tag(name = "Activity", description = "User activity and progress management")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "Create an activity", description = "Adds a new task or activity for the current user")
    @PostMapping
    public ResponseEntity<Void> createActivity(
        @Valid @RequestBody CreateActivityDto dto,
        @AuthenticationPrincipal User user) {
        activityService.createActivity(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Complete an activity", description = "Awards experience points for completing activity units")
    @PostMapping("/complete")
    public ResponseEntity<Void> completeActivity(
        @Valid @RequestBody ActivityCompletionDto dto,
        @AuthenticationPrincipal User user
    ) {
        activityService.addExperience(user.getId(), dto.activityId(), dto.unitsCompleted());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user statistics", description = "Returns aggregated activity statistics for the current user")
    @GetMapping("/stats")
    public ResponseEntity<UserStatsDto> getActivityStats(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(activityService.getUserStats(user.getId()));
    }


    @Operation(summary = "Get activity feed", description = "Returns a list of recent activities to display in the feed")
    @GetMapping("/feed")
    public ResponseEntity<List<ActivityFeedItemDto>> getFeed(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.getActivityFeed(user.getId()));
    }


    @Operation(summary = "Delete an activity", description = "Permanently removes an activity by its ID")
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void>deleteActivity(
        @PathVariable("activityId") Long activityId,
        @AuthenticationPrincipal User user
    ) {
        activityService.deleteActivity(user.getId(), activityId);
        return ResponseEntity.noContent().build();
    }
}
