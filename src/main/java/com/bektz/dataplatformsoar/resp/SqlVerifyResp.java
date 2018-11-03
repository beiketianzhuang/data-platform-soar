package com.bektz.dataplatformsoar.resp;

import lombok.*;

/**
 * sql审核对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlVerifyResp {
    private String verifyInfo;
}
