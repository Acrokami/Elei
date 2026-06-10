package com.acrobtw.elei.domain.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.domain.user.dto.UserProfileResponse;
import com.acrobtw.elei.domain.user.service.UserService;
import com.acrobtw.elei.domain.user.service.UserStreakService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User profile operations")
public class UserController {
    private final UserService userService;
    private final UserStreakService userStreakService;



    @Operation(summary = "Get user profile", description = "Returns basic profile data for the currently authenticated user")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(userService.getUserProfile(principal.getName()));
    }

    @Operation(summary = "Activate daily protocol", description = "Updates user streak and awards daily bonus XP")
    @PostMapping("check-in")
    public ResponseEntity<String> dailyCheckIn(Principal principal) {
        return ResponseEntity.ok(userStreakService.processDailyCheckIn(principal.getName()));
    }
}
