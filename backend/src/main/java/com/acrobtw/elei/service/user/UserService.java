package com.acrobtw.elei.service.user;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.ResourceNotFoundException;
import com.acrobtw.elei.repository.UserRepository;

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


    @Transactional
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.changePassword(encodedPassword);
    }



}
