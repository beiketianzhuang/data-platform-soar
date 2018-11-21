package com.bektz.dataplatformsoar.sqlparser.druid;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.stat.TableStat;
import com.bektz.dataplatformsoar.sqlparser.ColumnItem;
import com.bektz.dataplatformsoar.sqlparser.druid.visitor.DefineMySqlSchemaStatVisitor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDruidSqlParSer {

    protected static final String ALL_COLUMNS_MARK = "*";
    private static final int SCHEMA_AND_TABLE_COUNT = 2;
    protected static final String UNKNOWN_TABLE = "UNKNOWN";


    public abstract Map<String, ColumnItem> parserSql(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, SQLStatement sqlStatement);


    /**
     * 获取最外层字段和别名
     *
     * @return
     */
    protected List<SQLSelectItem> parserOutermostColumns(SQLStatement sqlStatement) {
        SQLSelect select = ((SQLSelectStatement) sqlStatement).getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        return query.getSelectList();
    }


    /**
     * 查询的字段中出现了UNKNOWN_TABLE时
     * 说明表中有些字段无法确认是哪张表的，所以初步认为该字段属于所有表
     *
     * @return
     */
    protected Map<String, ColumnItem> getColumnItemsHasUnknownTable(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, Collection<TableStat.Column> columns) {

        Map<String, ColumnItem> columnItemMap = new HashMap<>();
        Map<TableStat.Name, TableStat> tables = mySqlSchemaStatVisitor.getTables();
        tables.forEach((key, value) -> {
            for (TableStat.Column column : columns) {
                ColumnItem columnItem = ColumnItem.builder().schemaName(getSchemaName(column)).tableName(getTableName(column)).columnName(column.getName()).build();
                columnItemMap.put(formatColumnsKey(getTableName(column), column.getName()), columnItem);
            }
        });
        return columnItemMap;
    }

    protected String getSchemaName(TableStat.Column column) {
        return getTableNameOrSchemaName(column, 0);
    }

    protected String getTableName(TableStat.Column column) {
        String tableName = getTableNameOrSchemaName(column, 1);
        return StringUtils.isBlank(tableName) ? column.getName() : tableName;
    }

    protected String getTableNameOrSchemaName(TableStat.Column column, int index) {
        String[] split = StringUtils.split(column.getTable(), ".");
        if (split.length == SCHEMA_AND_TABLE_COUNT) {
            return split[index];
        }
        return StringUtils.EMPTY;
    }

    protected String formatColumnsKey(String tableName, String columnName) {
        String[] split = StringUtils.split(tableName, ".");
        if (split.length == SCHEMA_AND_TABLE_COUNT) {
            tableName = split[1];
        }
        return tableName + "." + columnName;
    }

    protected String getSchemaName(TableStat.Name table) {
        return getTableNameOrSchemaName(table, 0);
    }

    protected String getTableName(TableStat.Name table) {
        String tableName = getTableNameOrSchemaName(table, 1);
        return StringUtils.isBlank(tableName) ? table.getName() : tableName;
    }

    protected String getTableNameOrSchemaName(TableStat.Name table, int index) {
        String[] split = StringUtils.split(table.getName(), ".");
        if (split.length == SCHEMA_AND_TABLE_COUNT) {
            return split[index];
        }
        return StringUtils.EMPTY;
    }

}
