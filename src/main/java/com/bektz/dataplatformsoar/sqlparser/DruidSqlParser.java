package com.bektz.dataplatformsoar.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.bektz.dataplatformsoar.sqlparser.visitor.DefineMySqlSchemaStatVisitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;

@Slf4j
public class DruidSqlParser {

    private static final String ALL_COLUMNS_MARK = "*";
    private static final int SCHEMA_AND_TABLE_COUNT = 2;
    private static final String UNKNOWN_TABLE = "UNKNOWN";


    public Set<ColumnItem> parserRealMetaData(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, MYSQL);
        DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor = new DefineMySqlSchemaStatVisitor();
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(mySqlSchemaStatVisitor);
        List<SQLSelectItem> outermostColumns = parserOutermostColumns(sqlStatement);
        return getColumnItems(mySqlSchemaStatVisitor, outermostColumns);
    }


    private Set<ColumnItem> getColumnItems(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, List<SQLSelectItem> outermostColumns) {
        Set<ColumnItem> columnItems = new HashSet<>(outermostColumns.size());
        boolean isAll = outermostColumns.stream().anyMatch(item -> ALL_COLUMNS_MARK.equals(item.toString()));
        Map<TableStat.Name, TableStat> tables = mySqlSchemaStatVisitor.getTables();
        String columnName = StringUtils.EMPTY;
        if (isAll) {
            columnName = ALL_COLUMNS_MARK;
            tables.forEach((key, value) -> columnItems.add(ColumnItem.builder().tableName(key.getName()).columnName(ALL_COLUMNS_MARK).build()));
        }
        for (SQLSelectItem outermostColumn : outermostColumns) {
            if (ALL_COLUMNS_MARK.equals(outermostColumn.toString())) {
                continue;
            }
            buildColumnItems(columnItems, outermostColumn, mySqlSchemaStatVisitor, columnName);
        }
        return columnItems;
    }

    private void buildColumnItems(Set<ColumnItem> columnItems, SQLSelectItem outermostColumn, DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, String columnName) {
        Map<TableStat.Name, TableStat> tables = mySqlSchemaStatVisitor.getTables();
        TableStat.Column column = mySqlSchemaStatVisitor.getColumn(outermostColumn.getExpr());
        String table = column.getTable();
        String[] split = StringUtils.split(table, ".");
        ColumnItem columnItem;

        if (split.length == SCHEMA_AND_TABLE_COUNT) {
            String alias = outermostColumn.getAlias();
            if (StringUtils.isBlank(alias)) alias = outermostColumn.toString();
            if (UNKNOWN_TABLE.equals(table)) {
                ColumnItem columnIt = ColumnItem
                        .builder()
                        .schemaName(split[0])
                        .tableName(split[1])
                        .columnName(StringUtils.isBlank(columnName) ? column.getName() : columnName)
                        .columnAlias(alias)
                        .build();

                tables.forEach((key, value) -> columnItems.add(columnIt));
            } else {
                columnItem = ColumnItem
                        .builder()
                        .tableName(split[1])
                        .schemaName(split[0])
                        .columnName(StringUtils.isBlank(columnName) ? column.getName() : columnName)
                        .columnAlias(alias)
                        .build();
                columnItems.add(columnItem);
            }
        }

        if (split.length != SCHEMA_AND_TABLE_COUNT) {
            String alias = outermostColumn.getAlias();
            if (StringUtils.isBlank(alias)) alias = outermostColumn.toString();
            if (UNKNOWN_TABLE.equals(table)) {
                String finalAlias = alias;
                tables.forEach((key, value) -> columnItems.add(ColumnItem
                        .builder()
                        .tableName(key.getName())
                        .columnName(StringUtils.isBlank(columnName) ? column.getName() : columnName)
                        .columnAlias(finalAlias)
                        .build()));
            } else {
                columnItem = ColumnItem
                        .builder()
                        .tableName(column.getTable())
                        .columnName(StringUtils.isBlank(columnName) ? column.getName() : columnName)
                        .columnAlias(alias)
                        .build();
                columnItems.add(columnItem);
            }
        }


    }


    /**
     * 获取最外层字段和别名
     *
     * @return
     */
    private List<SQLSelectItem> parserOutermostColumns(SQLStatement sqlStatement) {
        SQLSelect select = ((SQLSelectStatement) sqlStatement).getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        return query.getSelectList();
    }

    private MySqlSchemaStatVisitor getMySqlSchemaStatVisitor(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, MYSQL);
        DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor = new DefineMySqlSchemaStatVisitor();
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(mySqlSchemaStatVisitor);
        return mySqlSchemaStatVisitor;
    }

}
