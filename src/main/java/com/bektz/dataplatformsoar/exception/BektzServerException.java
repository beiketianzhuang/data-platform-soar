package com.bektz.dataplatformsoar.exception;

import org.springframework.http.HttpStatus;

public class BektzServerException extends BektzException {


    public BektzServerException(int code, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), code, message);
    }

    public BektzServerException(int status, int code, String message) {
        super(status, code, message);
    }

    public BektzServerException(int code, String message, Throwable cause) {
        super(message, cause);
    }
}
