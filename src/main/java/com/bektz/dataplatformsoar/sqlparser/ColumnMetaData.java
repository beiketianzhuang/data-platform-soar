package com.bektz.dataplatformsoar.sqlparser;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMetaData {

    private String column;
    private String columnAlias;
}
