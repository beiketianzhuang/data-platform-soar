package com.bektz.dataplatformsoar.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemaReq {

    @NotBlank(message = "数据库地址不能为空")
    private String address;
    @NotBlank(message = "数据库不能为空")
    private String schema;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "端口不能为空")
    private Integer port;
}
