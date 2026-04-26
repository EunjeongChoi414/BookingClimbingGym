package com.project.email.controller;

import com.project.common.exception.GlobalExceptionHandler;
import com.project.email.dto.SendEmailVerificationCodeReq;
import com.project.email.dto.VerifyEmailReq;
import com.project.email.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
@Import(GlobalExceptionHandler.class)
public class EmailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @Test
    @DisplayName("이메일 인증코드 발송 - 성공")
    void sendEmailVerificationCode_success() throws Exception {
        when(emailService.sendVerificationCode(anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        var req = new SendEmailVerificationCodeReq("test@gmail.com");

        MvcResult mvcResult = mockMvc.perform(post("/app/email/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
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
                .andExpect(jsonPath("$.code").value("4000"));
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