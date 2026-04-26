package com.acrobtw.elei.service;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.exception.ResourceNotFoundException;


import com.acrobtw.elei.dto.AuthenticationRequest;
import com.acrobtw.elei.dto.AuthenticationResponse;
import com.acrobtw.elei.repository.UserRepository;
import com.acrobtw.elei.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        var user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ResourceNotFoundException("Username not found in AuthenticationService"));
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }
}
