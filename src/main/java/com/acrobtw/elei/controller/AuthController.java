package com.acrobtw.elei.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.AuthRequest;
import com.acrobtw.elei.dto.RegisterRequestDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.repository.UserRepository;
import com.acrobtw.elei.service.UserService;

@RestController
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody AuthRequest request) {
        userService.registerNewUser(request.email(), request.username(), request.password());
        return ResponseEntity.ok("Everything done! " + request.username() + "");
}
}
