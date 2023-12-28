package com.dontbe.www.DontBeServer.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private T data;

    public static ApiResponse success(SuccessStatus status, Object data) {
        return ApiResponse.builder()
                .status(status.getStatusCode())
                .success(true)
                .message(status.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse success(int status, String message) {
        return ApiResponse.builder()
                .status(status)
                .success(true)
                .message(message)
                .build();
    }

    public static ApiResponse fail(int status, String message) {
        return ApiResponse.builder()
                .status(status)
                .success(false)
                .message(message)
                .build();
    }
}
