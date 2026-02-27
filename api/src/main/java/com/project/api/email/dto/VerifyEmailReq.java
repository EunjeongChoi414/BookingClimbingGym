package com.project.api.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailReq {

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank
    String email;

    @Positive(message="이메일 인증 코드가 틀렸습니다.")
    int code;
}
