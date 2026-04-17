package com.acrobtw.elei.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.AuthRequest;
import com.acrobtw.elei.dto.AuthResponse;
import com.acrobtw.elei.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        userService.registerNewUser(request.email(), request.username(), request.password());

        AuthResponse response = new AuthResponse(
        HttpStatus.OK.value(),
        "User registered successfully",
        request.username(),
        LocalDateTime.now()
    );

        return ResponseEntity.ok(response);
}
}
