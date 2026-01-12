package com.erp.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final Integer statusCode;

    public CustomException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
