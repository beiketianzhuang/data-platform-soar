package com.bektz.dataplatformsoar.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
    private int code = 0;

    private String message = EMPTY;

    @Builder.Default
    private Boolean succeed = false;

    private int status = 200;

    public static final GenericResponse SUCCESS = new GenericResponse(0, "处理成功", true, 200);

    public static final GenericResponse SERVER_ERROR = new GenericResponse(1001, "服务器异常，请稍后再试", false, 500);

    public static final GenericResponse CLIENT_ERROR = new GenericResponse(2001, "参数不正确", false, 400);

    public GenericResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
