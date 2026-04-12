package com.project.gym.controller;

import com.project.common.jwt.AuthToken;
import com.project.gym.GymTestApplication;
import com.project.gym.dto.BusinessHours;
import com.project.gym.dto.Pass;
import com.project.gym.dto.RegisterGymReq;
import com.project.gym.dto.VerifyBusinessReq;
import com.project.user.entity.User;
import com.project.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GymTestApplication.class)
@AutoConfigureMockMvc
class GymControllerTests {
    private static final String TEST_JWT_SECRET = "test-jwt-secret-key-for-testing-purposes-only-32";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String validToken;

    @BeforeEach
    void setUp() {
        User user = new User("test@test.com", "password", Clock.systemDefaultZone());
        userRepository.create(user);
        validToken = "Bearer " + AuthToken.issue(user.getId(), TEST_JWT_SECRET, Clock.fixed(Instant.now(), ZoneId.systemDefault())).getToken();
    }

    @Test
    @DisplayName("사업자 정보 인증하기")
    public void verifyBusiness_success() throws Exception {
        var req = new VerifyBusinessReq(
                "1234567890",
                "최은정",
                LocalDate.of(2010, 10, 10));

        mockMvc.perform(post("/app/gyms/business-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("사업자 정보 인증하기 - 잘못된 사업자등록번호")
    public void verifyBusiness_invalidBusinessNumber() throws Exception {
        var req = new VerifyBusinessReq(
                "123",
                "최은정",
                LocalDate.of(2010, 10, 10));

        mockMvc.perform(post("/app/gyms/business-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("사업자등록번호는 숫자 10자리여야 합니다."));
    }

    @Test
    @DisplayName("사업자 정보 인증하기 - 잘못된 사업자 이름")
    public void verifyBusiness_invalidName() throws Exception {
        var req = new VerifyBusinessReq(
                "1234567890",
                " ",
                LocalDate.of(2010, 10, 10));

        mockMvc.perform(post("/app/gyms/business-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"));
    }

    @Test
    @DisplayName("암장 등록하기 - 성공")
    public void registerGym() throws Exception {
        var pass = new Pass("5회 이용권", new BigDecimal(90000), 5, 90);
        List<Pass> passes = List.of(pass);

        List<BusinessHours> businessHours = new ArrayList<>();
        businessHours.add(new BusinessHours(DayOfWeek.MONDAY, null, null));
        businessHours.add(new BusinessHours(DayOfWeek.TUESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.WEDNESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.THURSDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.FRIDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SATURDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SUNDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));

        var req = new RegisterGymReq(
                "gymName",
                "gymAddress",
                passes,
                "02-2222-2222",
                businessHours,
                100,
                1
        );

        mockMvc.perform(post("/app/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("암장 등록하기 - 잘못된 영업시간. 7일치가 아님")
    public void registerGym_invalidBusinessHours() throws Exception {
        var pass = new Pass(
                "5회 이용권", new BigDecimal(90000), 5, 90);
        List<Pass> passes = new ArrayList<>();
        passes.add(pass);

        List<BusinessHours> businessHours = new ArrayList<>();
        businessHours.add(new BusinessHours(DayOfWeek.MONDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.TUESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));

        var req = new RegisterGymReq(
                "gymName",
                "gymAddress",
                passes,
                "02-2222-2222",
                businessHours,
                100,
                1
        );

        mockMvc.perform(post("/app/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("영업시간은 정확히 7개여야 합니다."));
    }

    @Test
    @DisplayName("암장 등록하기 - 패스가 하나도 없음")
    public void registerGym_emptyPasses() throws Exception {
        List<BusinessHours> businessHours = new ArrayList<>();
        businessHours.add(new BusinessHours(DayOfWeek.MONDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.TUESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.WEDNESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.THURSDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.FRIDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SATURDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SUNDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));

        var req = new RegisterGymReq(
                "gymName",
                "gymAddress",
                new ArrayList<>(),
                "02-2222-2222",
                businessHours,
                100,
                1
        );

        mockMvc.perform(post("/app/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value("false"))
                .andExpect(jsonPath("$.code").value("4000"))
                .andExpect(jsonPath("$.message").value("적어도 하나 이상의 패스를 제공해야합니다."));
    }
}