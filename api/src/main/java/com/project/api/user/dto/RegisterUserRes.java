package com.project.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserRes {
    private String jwt;
    private String userId;
}
