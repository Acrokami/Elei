package com.acrobtw.elei.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.entity.Activity;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.ActivityNotFoundException;
import com.acrobtw.elei.exception.UserNotFoundException;
import com.acrobtw.elei.repository.ActivityRepository;
import com.acrobtw.elei.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@SuppressWarnings("unused")
public class UserService {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(
        UserRepository userRepository,
        ActivityRepository activityRepository,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityRepository = activityRepository;
    }

    public void registerNewUser(String username, String email, String password) {
        if(userRepository.findByEmail(email).isPresent()) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashPassword);
        userRepository.save(user);
    }


    @Transactional
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.changePassword(encodedPassword);
    }

    // TODO: Add experience to user when they complete an activity, and calculate the level up if necessary
    @Transactional
    public void addExperience(Long userId, Long activityId, double multiplier) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

        Activity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new ActivityNotFoundException(activityId));
    }
}
