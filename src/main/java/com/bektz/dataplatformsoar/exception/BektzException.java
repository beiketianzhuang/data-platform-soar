package com.bektz.dataplatformsoar.exception;

import lombok.Data;

@Data
public class BektzException extends RuntimeException {
    private int code;
    private int status;
    private String message;

    public BektzException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BektzException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BektzException(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BektzException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}
