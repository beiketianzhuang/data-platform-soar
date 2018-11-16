package com.bektz.dataplatformsoar.exception;

import org.springframework.http.HttpStatus;

public class BektzClientException extends RuntimeException {
    private int code;
    private int status;
    private String message;

    public BektzClientException() {
    }

    public BektzClientException(int code, String message) {
        this.code = code;
        this.status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        this.message = message;
    }

    public BektzClientException(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BektzClientException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
