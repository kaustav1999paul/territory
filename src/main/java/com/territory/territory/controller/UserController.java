package com.territory.territory.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.dto.UserRegistrationRequest;
import com.territory.territory.dto.UserResponse;
import com.territory.territory.entity.User;
import com.territory.territory.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request
    ) {

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
}