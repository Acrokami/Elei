package com.acrobtw.elei.domain.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.acrobtw.elei.domain.user.dto.UpdateEmailRequest;
import com.acrobtw.elei.domain.user.dto.UpdatePasswordRequest;
import com.acrobtw.elei.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Endpoints for updating user settings such as email and password")
public class SettingsController {

    private final UserService userService;

    @Operation(summary = "Update email", description = "Allows the authenticated user to update their registered email address")
    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(Principal principal, @Valid @RequestBody UpdateEmailRequest request) {
        try {
            userService.updateEmail(principal.getName(), request.newEmail());
            return ResponseEntity.ok().body("Email successfully updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update password", description = "Allows the authenticated user to update their password by providing the current password and a new password")
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(Principal principal, @Valid @RequestBody UpdatePasswordRequest request) {
        try {
            userService.updatePassword(principal.getName(), request.currentPassword(), request.newPassword());
            return ResponseEntity.ok().body("Security code successfully updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
