package com.project.api.user;

import com.project.api.user.dto.RegisterUserReq;
import com.project.common.AuthToken;
import com.project.common.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTests {

    private static final String TEST_SECRET = "test-secret-key-for-testing-purposes-only-32c";
    private static final String TEST_JWT_SECRET = "test-jwt-secret-key-for-testing-purposes-only-32";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("자동 로그인 - 성공")
    public void autoLogin_success() throws Exception {
        var validToken = AuthToken.issue("some-user-id", TEST_JWT_SECRET).getToken();

        mockMvc.perform(get("/app/users/auto-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("자동 로그인 - 토큰 없음")
    public void autoLogin_tokenAbsent() throws Exception {
        mockMvc.perform(get("/app/users/auto-login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("2001"))
                .andExpect(jsonPath("$.message").value("회원가입이 필요합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("유저 회원가입 - 성공")
    public void registerUser_success() throws Exception {
        var ticket = Ticket.issue("test@example.com", TEST_SECRET).getToken();
        var req = new RegisterUserReq(
                "test@example.com",
                "examplepassword123",
                "examplepassword123",
                ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/app/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("유저 회원가입 - 이메일 형식 잘못됨")
    public void registerUser_invalidEmail() throws Exception {
        var req = new RegisterUserReq(
                "test",
                "examplePassword123",
                "examplePassword123",
                "validTicket");

        mockMvc.perform(MockMvcRequestBuilders.post("/app/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("이메일 형식이 잘못되었습니다."));
    }

    @Test
    @DisplayName("유저 회원가입 - 비밀번호 길이 형식 틀림")
    public void registerUser_invalidPasswordLength() throws Exception {
        var req = new RegisterUserReq(
                "test@example.com",
                "shortPw",
                "shortPw",
                "validTicket");

        mockMvc.perform(MockMvcRequestBuilders.post("/app/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("비밀번호 길이는 8~20자 사이여야 합니다."));
    }

    @Test
    @DisplayName("유저 회원가입 - 비밀번호 확인이 틀림")
    public void registerUser_invalidPasswordCheck() throws Exception {
        var ticket = Ticket.issue("test@example.com", TEST_SECRET).getToken();
        var req = new RegisterUserReq(
                "test@example.com",
                "originalPassword",
                "differentPassword",
                ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/app/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("2003"))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("유저 회원가입 - 빈 티켓")
    public void registerUser_emptyTicket() throws Exception {
        var req = new RegisterUserReq(
                "test@example.com",
                "examplePassword123",
                "examplePassword123",
                " ");

        mockMvc.perform(MockMvcRequestBuilders.post("/app/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"));
    }
}