package com.acrobtw.elei.domain.auth;



import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.core.security.jwt.JwtService;
import com.acrobtw.elei.domain.auth.dto.LoginRequest;
import com.acrobtw.elei.domain.auth.dto.LoginResponse;
import com.acrobtw.elei.domain.auth.dto.RegisterRequest;
import com.acrobtw.elei.domain.auth.dto.RegisterResponse;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.service.UserService;

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
