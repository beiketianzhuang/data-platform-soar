package com.bektz.dataplatformsoar.exception;

import org.springframework.http.HttpStatus;

public class BektzClientException extends BektzException {

    public BektzClientException(int code, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY.value(), code, message);
    }

    public BektzClientException(int status, int code, String message) {
        super(status, code, message);
    }

    public BektzClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
