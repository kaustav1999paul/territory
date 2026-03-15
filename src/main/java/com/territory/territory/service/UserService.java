package com.territory.territory.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.territory.territory.dto.UserRegistrationRequest;
import com.territory.territory.entity.User;
import com.territory.territory.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registers a new user and returns the saved user entity
    public User register(UserRegistrationRequest request) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setImageUrl(request.getImageUrl());

        return userRepository.save(user);
    }
}