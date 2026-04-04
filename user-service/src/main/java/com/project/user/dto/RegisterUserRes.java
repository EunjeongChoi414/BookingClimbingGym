package com.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserRes {
    private String jwt;
    private String userId;
}
