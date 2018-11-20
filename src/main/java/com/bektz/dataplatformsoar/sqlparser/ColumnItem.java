package com.bektz.dataplatformsoar.sqlparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnItem {
    private String schemaName;
    private String tableAlias;
    private String tableName;
    private String columnName;
    private String columnAlias;

    @Builder.Default
    private boolean identity = false;

    public boolean getIdentity() {
        return StringUtils.equalsIgnoreCase(columnAlias, columnName);
    }
}