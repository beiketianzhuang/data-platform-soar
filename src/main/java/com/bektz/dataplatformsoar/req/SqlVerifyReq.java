package com.bektz.dataplatformsoar.req;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class SqlVerifyReq {
    private String sql;
}
