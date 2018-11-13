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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TableMetaData that = (TableMetaData) o;

        return table != null ? table.equals(that.table) : that.table == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (table != null ? table.hashCode() : 0);
        return result;
    }
}
