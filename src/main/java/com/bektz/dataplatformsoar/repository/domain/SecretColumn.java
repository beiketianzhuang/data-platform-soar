package com.bektz.dataplatformsoar.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretColumn {

    private Long id;

    //数据库名
    private String schemaName;

    //表名
    private String table;

    //字段
    private String column;

    //是否敏感字段：1-敏感，0-非敏感
    private Boolean isSecret;

    //字段类型（MOBILE, CARD_NO, ID_NO）
    private String columnType;

    //敏感类型：SECRECT（敏感）, SUSPECTED_SECRET（疑似敏感）
    private String secretType;

    private String createdBy;

    private String updatedBy;
}
