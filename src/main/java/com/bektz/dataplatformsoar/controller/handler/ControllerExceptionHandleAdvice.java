package com.bektz.dataplatformsoar.controller.handler;

import com.bektz.dataplatformsoar.exception.BektzClientException;
import com.bektz.dataplatformsoar.exception.BektzException;
import com.bektz.dataplatformsoar.exception.BektzServerException;
import com.bektz.dataplatformsoar.resp.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionHandleAdvice {

    @ExceptionHandler
    public ResponseEntity<GenericResponse> handleException(BektzException e) {
        GenericResponse genericResponse = GenericResponse.builder().code(e.getCode()).status(e.getStatus()).message(e.getMessage()).build();
        if (e instanceof BektzServerException) {
            log.error("[ServerError]: ", e);
        } else if (e instanceof BektzClientException) {
            log.warn("[ClientWarn]: ", e);
        } else {
            return new ResponseEntity<>(GenericResponse.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(genericResponse, HttpStatus.valueOf(e.getStatus()));
    }
}