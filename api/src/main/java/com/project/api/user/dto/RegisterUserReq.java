package com.project.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserReq {
    @Email(message="이메일 형식이 잘못되었습니다.")
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 20, message = "비밀번호 길이는 8~20자 사이여야 합니다.")
    private String password;

    @NotBlank
    @Length(min = 8, max = 20, message = "비밀번호 길이는 8~20자 사이여야 합니다.")
    private String passwordConfirm;

    @NotBlank
    private String ticket;
}
