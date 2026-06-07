package com.acrobtw.elei.domain.user.service;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.quest.service.QuestEngineService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.dto.UserProfileResponse;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestEngineService questEngineService;


    @Transactional
    public User registerNewUser(String username, String email, String password) {
        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashPassword);
        User savedUser = userRepository.save(user);
        questEngineService.assignInitialQuests(savedUser);

        return savedUser;
    }

    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", username));

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
