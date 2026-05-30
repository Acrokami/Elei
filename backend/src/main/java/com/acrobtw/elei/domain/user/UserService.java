package com.acrobtw.elei.domain.user;


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

        return new UserProfileResponse(user.getUsername(), user.getEmail());
    }

    @Transactional
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.changePassword(encodedPassword);
    }



}
