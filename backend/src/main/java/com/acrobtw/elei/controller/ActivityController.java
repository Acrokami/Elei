package com.acrobtw.elei.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.ActivityCompletionDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final UserService userService;


    @PostMapping("/complete")
    public ResponseEntity<Void> completeActivity(
        @RequestBody ActivityCompletionDto dto,
        @AuthenticationPrincipal User user
    ) {
        userService.addExperience(user.getId(), dto.activityId(), dto.multiplier());
        return ResponseEntity.ok().build();
    }
}
