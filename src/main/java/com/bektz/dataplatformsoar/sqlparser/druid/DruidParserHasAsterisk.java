package com.bektz.dataplatformsoar.sqlparser.druid;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.stat.TableStat;
import com.bektz.dataplatformsoar.sqlparser.ColumnItem;
import com.bektz.dataplatformsoar.sqlparser.druid.visitor.DefineMySqlSchemaStatVisitor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * 最外层查询有 * 时
 *
 * @return
 */
@Component("hasAsterisk")
public class DruidParserHasAsterisk extends AbstractDruidSqlParSer {

    @Override
    public Map<String, ColumnItem> parserSql(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, SQLStatement sqlStatement) {
        Map<String, ColumnItem> columnItemMap = new HashMap<>();
        Collection<TableStat.Column> columns = mySqlSchemaStatVisitor.getColumns();
        for (TableStat.Column column : columns) {
            if (column.isSelect()) {
                ColumnItem columnItem = ColumnItem.builder().schemaName(getSchemaName(column)).tableName(getTableName(column)).columnName(column.getName()).build();
                columnItemMap.put(formatColumnsKey(column.getTable(), column.getName()), columnItem);
            }
        }
        Set<TableStat.Name> tables = mySqlSchemaStatVisitor.getTables().keySet();
        Set<String> itemTables = columnItemMap.values().stream().map(ColumnItem::getTableName).collect(toSet());
        for (TableStat.Name table : tables) {
            String tableName = getTableName(table);
            if (!itemTables.contains(tableName)) {
                ColumnItem columnItem = ColumnItem.builder().schemaName(getSchemaName(table)).tableName(tableName).columnName(ALL_COLUMNS_MARK).build();
                columnItemMap.put(formatColumnsKey(tableName, ALL_COLUMNS_MARK), columnItem);
            }
        }

        return columnItemMap;
    }
}
