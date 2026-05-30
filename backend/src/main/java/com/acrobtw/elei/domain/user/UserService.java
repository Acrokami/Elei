package com.acrobtw.elei.domain.user;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.user.dto.UserProfileResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerNewUser(String username, String email, String password) {
        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashPassword);
        return userRepository.save(user);
    }

    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("No citizen with this username"));

        List<String> activeDates = user.getExperienceLogs().stream()
            .map(log -> log.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
            .distinct()
            .collect(Collectors.toList());

        if (user.getLastActivityDate() != null) {
            String lastActivity = user.getLastActivityDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            if (!activeDates.contains(lastActivity)) {
                activeDates.add(lastActivity);
            }
        }

        return new UserProfileResponse(
            user.getUsername(),
            user.getEmail(),
            activeDates 
        );
    }

    @Transactional
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.changePassword(encodedPassword);
    }



}
