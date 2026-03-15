package com.territory.territory.common.api;

public class ApiResponse<T> {

    private int statusCode;
    private String errorMessage;
    private T responseBody;

    public ApiResponse(int statusCode, String errorMessage, T responseBody) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.responseBody = responseBody;
    }

    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>(200, null, body);
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return new ApiResponse<>(statusCode, message, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getResponseBody() {
        return responseBody;
    }
}