package com.bektz.dataplatformsoar.exception;

import org.springframework.http.HttpStatus;

public class BektzServerException extends RuntimeException {

    private int code;
    private int status;
    private String message;

    public BektzServerException() {
    }

    public BektzServerException(int code, String message) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = code;
        this.message = message;
    }

    public BektzServerException(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BektzServerException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
