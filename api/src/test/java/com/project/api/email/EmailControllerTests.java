package com.project.api.email;

import com.project.api.email.dto.SendEmailVerificationCodeReq;
import com.project.api.email.dto.VerifyEmailReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이메일 인증코드 발송 - 성공")
    void sendEmailVerificationCode_success() throws Exception {
        var req = new SendEmailVerificationCodeReq("test@gmail.com");

        mockMvc.perform(post("/app/email/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.message").value("요청에 성공했습니다."));
    }

    @Test
    @DisplayName("이메일 인증코드 발송 - 잘못된 이메일 형식")
    void sendEmailVerificationCode_invalidEmail() throws Exception {
        var req = new SendEmailVerificationCodeReq("test");

        mockMvc.perform(post("/app/email/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("이메일 형식이 잘못되었습니다."));
    }

    @Test
    @DisplayName("이메일 인증코드 확인 실패- 잘못된 이메일 형식")
    void verifyEmail_invalidEmail() throws Exception {
        var req = new VerifyEmailReq("test", "123456");

        mockMvc.perform(post("/app/email/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("이메일 형식이 잘못되었습니다."));
    }
}
