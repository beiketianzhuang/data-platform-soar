package com.bektz.dataplatformsoar.sqlparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMetaData {

    private String table;
    private String tableAlias;
    private List<ColumnMetaData> columnMetaDatas = new ArrayList<>();
}
