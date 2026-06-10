package com.acrobtw.elei.domain.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.domain.auth.AuthenticationService;
import com.acrobtw.elei.domain.auth.dto.LoginRequest;
import com.acrobtw.elei.domain.auth.dto.LoginResponse;
import com.acrobtw.elei.domain.auth.dto.RegisterRequest;
import com.acrobtw.elei.domain.auth.dto.RegisterResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user registration and authentication")
public class AuthController {
    private final AuthenticationService authenticationService;


    @Operation(summary = "Register a new user", description = "Creates a new user account in the Elei system")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
  }

    @Operation(summary = "Authenticate user", description = "Returns a JWT token to access protected endpoints")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
