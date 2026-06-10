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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(Principal principal, @RequestBody UpdateEmailRequest request) {
        try {
            userService.updateEmail(principal.getName(), request.newEmail());
            return ResponseEntity.ok().body("Email successfully updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(Principal principal, @RequestBody UpdatePasswordRequest request) {
        try {
            userService.updatePassword(principal.getName(), request.currentPassword(), request.newPassword());
            return ResponseEntity.ok().body("Security code successfully updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
