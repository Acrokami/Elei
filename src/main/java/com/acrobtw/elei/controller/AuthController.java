package com.acrobtw.elei.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.AuthenticationRequest;
import com.acrobtw.elei.dto.AuthenticationResponse;
import com.acrobtw.elei.dto.RegisterResponse;
import com.acrobtw.elei.service.AuthenticationService;
import com.acrobtw.elei.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody AuthenticationRequest request) {
        userService.registerNewUser(request.email(), request.username(), request.password());

        RegisterResponse response = new RegisterResponse(
        HttpStatus.OK.value(),
        "User registered successfully",
        request.username(),
        LocalDateTime.now()
    );

        return ResponseEntity.ok(response);
  }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
