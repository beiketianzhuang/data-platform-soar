package com.bektz.dataplatformsoar.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemaReq {
    private String url;
    private String schema;
    private String userName;
    private String password;
    private String port;
}
