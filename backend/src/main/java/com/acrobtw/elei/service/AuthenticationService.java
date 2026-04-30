package com.acrobtw.elei.service;



import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.exception.ResourceNotFoundException;
import com.acrobtw.elei.dto.auth.LoginResponse;
import com.acrobtw.elei.dto.auth.RegisterRequest;
import com.acrobtw.elei.dto.auth.RegisterResponse;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.dto.auth.LoginRequest;
import com.acrobtw.elei.repository.UserRepository;
import com.acrobtw.elei.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        var user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ResourceNotFoundException("Username not found in AuthenticationService"));
        var jwtToken = jwtService.generateToken(user);

        return new LoginResponse(
            HttpStatus.OK.value(),
            request.username(),
            jwtToken,
            LocalDateTime.now());
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = userService.registerNewUser(request.username(), request.email(), request.password());

        var jwtToken = jwtService.generateToken(user);

        return new RegisterResponse(
            HttpStatus.OK.value(),
            "Register successfully completed",
            request.username(),
            jwtToken,
            LocalDateTime.now()
        );
    }
}
