package com.bektz.dataplatformsoar.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
    private int code = 0;

    private String message = EMPTY;

    private Boolean succeed = true;

    private int status = 200;

    public static final GenericResponse SUCCESS = new GenericResponse();

}
