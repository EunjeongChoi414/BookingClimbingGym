package com.project.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailVerificationCodeReq {

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank
    private String email;
}
