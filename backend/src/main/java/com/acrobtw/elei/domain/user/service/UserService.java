package com.acrobtw.elei.domain.user.service;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.auth.enums.AuthProvider;
import com.acrobtw.elei.domain.auth.enums.Role;
import com.acrobtw.elei.domain.experience.LevelService;
import com.acrobtw.elei.domain.quest.service.QuestEngineService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;
import com.acrobtw.elei.domain.user.dto.UserProfileResponse;
import com.acrobtw.elei.domain.user.event.UserRegisteredEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestEngineService questEngineService;
    private final LevelService levelService;
    private final ApplicationEventPublisher eventPublisher;


    @Value("${app.admin.username}")
    private String adminUsername;

    @Transactional
    public User registerNewUser(String username, String email, String password) {
        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashPassword);
        user.setRole(username.equals(adminUsername) ? Role.ADMIN : Role.USER);
        User savedUser = userRepository.save(user);
        questEngineService.assignInitialQuests(savedUser);

        log.info("[SYSTEM] Citizen {} successfully registered.", savedUser.getUsername());
        eventPublisher.publishEvent(new UserRegisteredEvent(savedUser));
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

        Long currentXp = user.getTotalExperience();
        Long currentLevel = levelService.calculateLevel(currentXp);
        Long nextLevelXp = levelService.calculateNextLevelUp(currentLevel);
        String rank = determineRank(currentLevel);


        return new UserProfileResponse(
        user.getUsername(),
        user.getEmail(),
        user.getProvider(),
        activeDates,
        currentLevel.intValue(),
        currentXp,
        nextLevelXp,
        rank,
        user.getRole().name()
        );
    }

    private String determineRank(Long level) {
        if (level < 5) return "Initiate";
        if (level < 10) return "Adept";
        if (level < 25) return "Veteran";
        if (level < 50) return "Elite";
        return "Grandmaster";
    }


    @Transactional
    public void updateEmail(String username, String newEmail) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));
        if(userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("This communication link (email) is already in use.");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
        log.info("[SYSTEM] Citizen {} successfully updated their email.", username);
    }

    @Transactional
    public void updatePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));

       if (user.getProvider() != null && user.getProvider() != AuthProvider.LOCAL) {
        throw new IllegalArgumentException(
        "Security clearance denied: Your identity is managed by an external provider (" + user.getProvider().name() + ")."
    );
}

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid current security authorization code.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("[SYSTEM] Citizen {} successfully rotated their security code.", username);
    }
}
