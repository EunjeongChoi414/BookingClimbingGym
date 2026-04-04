package com.project.gym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetGymCrowdednessReq {

    @NotNull
    @JsonFormat(pattern="yyyy-MM-ddTHH:mm")
    private LocalDateTime dateTime;;
}
