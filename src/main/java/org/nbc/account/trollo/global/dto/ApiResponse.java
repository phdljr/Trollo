package org.nbc.account.trollo.global.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private int status;
    private String message;
    private final T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

}
