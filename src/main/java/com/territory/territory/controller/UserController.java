package com.territory.territory.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.dto.LoginRequest;
import com.territory.territory.dto.LoginResponse;
import com.territory.territory.dto.UserRegistrationRequest;
import com.territory.territory.dto.UserResponse;
import com.territory.territory.entity.User;
import com.territory.territory.repository.UserRepository;
import com.territory.territory.security.JwtUtil;
import com.territory.territory.service.UserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public UserController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userService.register(request);
        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getImageUrl(),
                user.getCreatedAt()
        );
        return ApiResponse.success(response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getId().toString());
        return ApiResponse.success(new LoginResponse(token));
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> profile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getImageUrl(),
            user.getCreatedAt()
        );

        return ApiResponse.success(response);
    }
}