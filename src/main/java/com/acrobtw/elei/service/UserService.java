package com.acrobtw.elei.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.UserNotFoundException;
import com.acrobtw.elei.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

}
