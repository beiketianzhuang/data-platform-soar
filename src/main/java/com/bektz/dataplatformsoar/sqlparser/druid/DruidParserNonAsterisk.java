package com.bektz.dataplatformsoar.sqlparser.druid;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.stat.TableStat;
import com.bektz.dataplatformsoar.sqlparser.ColumnItem;
import com.bektz.dataplatformsoar.sqlparser.druid.visitor.DefineMySqlSchemaStatVisitor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * sql最外层没有*号时
 */
@Component("nonAsterisk")
public class DruidParserNonAsterisk extends AbstractDruidSqlParSer {

    @Override
    public Map<String, ColumnItem> parserSql(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, SQLStatement sqlStatement) {
        LinkedList<TableStat.Column> columns = new LinkedList<>();
        Map<String, ColumnItem> columnItemMap = new HashMap<>();
        List<SQLSelectItem> outermostColumns = parserOutermostColumns(sqlStatement);
        for (SQLSelectItem outermostColumn : outermostColumns) {
            TableStat.Column column = mySqlSchemaStatVisitor.getColumn(outermostColumn.getExpr());
            if (UNKNOWN_TABLE.equals(column.getTable())) {
                columns.addFirst(column);
            }
            if (!UNKNOWN_TABLE.equals(column.getTable())) {
                ColumnItem columnItem = ColumnItem.builder().schemaName(getSchemaName(column)).tableName(getTableName(column)).columnName(column.getName()).build();
                columnItemMap.put(formatColumnsKey(column.getTable(), column.getName()), columnItem);
            }
        }
        Map<String, ColumnItem> columnItems = getColumnItemsHasUnknownTable(mySqlSchemaStatVisitor, columns);
        columnItemMap.putAll(columnItems);
        return columnItemMap;
    }


}
