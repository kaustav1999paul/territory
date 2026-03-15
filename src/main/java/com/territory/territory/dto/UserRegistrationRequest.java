package com.territory.territory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {

    private String email;
    private String username;
    private String password;
    private String imageUrl;

}