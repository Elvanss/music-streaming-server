package com.music.ms_user.builder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.ms_user.api.ApiResponse;

public class ResponseBuilder {

    private ResponseBuilder() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, message, null));
    }
}

