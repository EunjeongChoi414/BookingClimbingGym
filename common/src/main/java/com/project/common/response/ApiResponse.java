package com.project.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    @JsonProperty("isSuccess")
    private final boolean isSuccess;
    private final int code;
    private final String message;
    private final T data;

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, 1000, "요청에 성공했습니다.", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 1000, "요청에 성공했습니다.", data);
    }

    public static ApiResponse<Void> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ApiResponse<Void> fail(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
