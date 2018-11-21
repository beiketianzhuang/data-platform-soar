package com.bektz.dataplatformsoar.req;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class SqlVerifyReq {
    @NotBlank(message = "sql语句不能为空")
    private String sql;
    @NotBlank(message = "请选择数据库后再执行sql")
    private String schema;
}
