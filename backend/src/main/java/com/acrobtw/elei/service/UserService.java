package com.acrobtw.elei.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.entity.Activity;
import com.acrobtw.elei.entity.ExperienceLog;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.ResourceNotFoundException;
import com.acrobtw.elei.repository.ActivityRepository;
import com.acrobtw.elei.repository.ExperienceLogRepository;
import com.acrobtw.elei.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ExperienceLogRepository experienceLogRepository;
    private final PasswordEncoder passwordEncoder;


    public void registerNewUser(String username, String email, String password) {
        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashPassword);
        userRepository.save(user);
    }


    @Transactional
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.changePassword(encodedPassword);
    }

    
    @Transactional
    public void addExperience(Long userId, Long activityId, double multiplier) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Activity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        int experience = activity.getBaseExperience();
        int finalExperience = (int) Math.round(experience * multiplier);

        ExperienceLog log = new ExperienceLog(finalExperience);

        user.addExperienceLog(log);
        activity.addExperienceLog(log);

        user.setTotalExperience(user.getTotalExperience() + finalExperience);
    }
}
