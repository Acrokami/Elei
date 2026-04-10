package com.acrobtw.elei.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.repository.UserRepository;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(String email, String username, String password) {
        if(userRepository.findByEmail(email).isPresent()) throw new IllegalArgumentException("This email is taken");

        String hashPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(hashPassword);

        userRepository.save(user);
    }

}
