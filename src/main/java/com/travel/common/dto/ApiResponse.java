package com.travel.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;          // The actual data to be returned
    private String message;  // A message, usually used for error or additional info

    // Static helper method for successful responses
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>( data, message);
    }
    // Static helper method for failed responses
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, message );
    }
}
