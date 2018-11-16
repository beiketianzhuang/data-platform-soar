package com.bektz.dataplatformsoar.configs;

import com.bektz.dataplatformsoar.resp.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;


@Slf4j
@Aspect
@Configuration
public class ParamValidAop {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public void excude() {
    }

    @Around("excude()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.getErrorCount() > 0) {
                    List<ObjectError> allErrors = result.getAllErrors();
                    StringBuilder sb = new StringBuilder();
                    for (ObjectError allError : allErrors) {
                        sb.append(allError.getDefaultMessage()).append(";");
                    }
                    return ResponseEntity.badRequest().body(GenericResponse.CLIENT_ERROR.setMessage(sb.toString()));
                }
            }
        }
        return pjp.proceed();
    }

}