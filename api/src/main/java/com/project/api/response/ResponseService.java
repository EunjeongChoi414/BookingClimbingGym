package com.project.api.response;

import com.project.api.response.exception.ExceptionStatus;

public class ResponseService {
    public <T> BaseResponse<T> getSuccessResponse() {
        return new BaseResponse<>(true, 1000, "요청에 성공했습니다.", null);
    }

    public <T> BaseResponse<T> getSuccessResponse(T data) {
        return new BaseResponse<>(true, 1000, "요청에 성공했습니다.", data);
    }

    public <T> BaseResponse<T> getFailureResponse(ExceptionStatus status) {
        return new BaseResponse<>(
                status.isSuccess(),
                status.getCode(),
                status.getMessage(),
                null
        );
    }

    public BaseResponse<String> getArgumentFailedResponse(String message) {
        return new BaseResponse<>(
                false,
                4000,
                message,
                null
        );
    }
}
