package com.acrobtw.elei.domain.user;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.user.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        return ResponseEntity.ok(new UserProfileResponse(user.getUsername(), user.getEmail()));
    }
}
