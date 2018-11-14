package com.bektz.dataplatformsoar.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.bektz.dataplatformsoar.exception.SqlParserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;

@Slf4j
public class SqlAnalyzer {
    private static final int INPUT_SQL_COUNT_MAX = 1;

    public Set<TableMetaData> parserRealMetaData(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, MYSQL);
        if (stmtList.size() > INPUT_SQL_COUNT_MAX) {
            throw new SqlParserException("不好意思哦！只支持单条sql的查询");
        }

        SchemaStatVisitor statVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(statVisitor);

        Map<String, List<String>> allColumns = parserAllColumns(statVisitor);
        List<SQLSelectItem> outermostColumns = parserOutermostColumns(sqlStatement);
        List<SQLSelectItem> subqueryColumns = parserSubqueryColumns(sqlStatement);
        return parserRealOutermostColumns(allColumns, outermostColumns, subqueryColumns);
    }

    /**
     * sql真实需要查的字段和表的关系
     *
     * @param allColumns
     * @param outermostColumns
     * @param subqueryColumns
     * @return
     */
    private Set<TableMetaData> parserRealOutermostColumns(Map<String, List<String>> allColumns,
                                                          List<SQLSelectItem> outermostColumns,
                                                          List<SQLSelectItem> subqueryColumns) {
        Set<TableMetaData> metaDatas = new HashSet<>(outermostColumns.size());
        Set<String> tables = allColumns.keySet();
        if (CollectionUtils.isEmpty(subqueryColumns)) {
            buildParserTableMetaData(allColumns, outermostColumns, metaDatas, tables);
        }

        if (!CollectionUtils.isEmpty(subqueryColumns)) {
            buildParserTableMetaData(allColumns, outermostColumns, subqueryColumns, metaDatas, tables);
        }
        return metaDatas;
    }

    private void buildParserTableMetaData(Map<String, List<String>> allColumns,
                                          List<SQLSelectItem> outermostColumns,
                                          List<SQLSelectItem> subqueryColumns,
                                          Set<TableMetaData> metaDatas,
                                          Set<String> tables) {

        for (SQLSelectItem outermostColumn : outermostColumns) {
            SQLExpr expr = outermostColumn.getExpr();
            String outerName = null;
            if (expr instanceof SQLName) {
                outerName = ((SQLName) expr).getSimpleName();
            }
            if (expr instanceof SQLAllColumnExpr) {
                metaDatas.add(TableMetaData.builder().table("*").build());
                continue;
            }
            a:
            for (SQLSelectItem subqueryColumn : subqueryColumns) {
                String subAlias = subqueryColumn.getAlias();
                if (outerName.equals(subAlias)) {
                    SQLExpr subExpr = subqueryColumn.getExpr();
                    String subqueryName = null;
                    if (expr instanceof SQLName) {
                        subqueryName = ((SQLName) subExpr).getSimpleName();
                    }
                    for (String table : tables) {
                        TableMetaData tableMetaData = null;
                        for (TableMetaData metaData : metaDatas) {
                            if (metaData.getTable().equals(table)) {
                                tableMetaData = metaData;
                                break;
                            }
                        }
                        if (tableMetaData == null) tableMetaData = new TableMetaData();
                        List<ColumnMetaData> columnMetaDatas = tableMetaData.getColumnMetaDatas();
                        List<String> columns = allColumns.get(table);
                        if (columns.contains(subqueryName)) {
                            tableMetaData.setTable(table);
                            String alias = outermostColumn.getAlias();
                            if (StringUtils.isBlank(alias)) alias = outermostColumn.toString();
                            columnMetaDatas.add(ColumnMetaData.builder().column(subqueryName).columnAlias(alias).build());
                            metaDatas.add(tableMetaData);
                            continue a;
                        }

                    }
                }

            }
        }
    }

    private void buildParserTableMetaData(Map<String, List<String>> allColumns,
                                          List<SQLSelectItem> outermostColumns,
                                          Set<TableMetaData> metaDatas,
                                          Set<String> tables) {

        for (String table : tables) {
            TableMetaData tableMetaData = new TableMetaData();
            List<ColumnMetaData> columnMetaDatas = tableMetaData.getColumnMetaDatas();
            a:
            for (SQLSelectItem outermostColumn : outermostColumns) {
                SQLExpr expr = outermostColumn.getExpr();
                String name = null;
                if (expr instanceof SQLName) {
                    name = ((SQLName) expr).getSimpleName();
                }
                if (expr instanceof SQLAllColumnExpr) {
                    metaDatas.add(TableMetaData.builder().table("*").build());
                    continue a;
                }
                if (allColumns.get(table).contains(name)) {
                    String alias = outermostColumn.getAlias();
                    if (StringUtils.isBlank(alias)) alias = outermostColumn.toString();
                    columnMetaDatas.add(ColumnMetaData.builder().column(name).columnAlias(alias).build());
                    tableMetaData.setTable(table);
                }
            }
            if (!CollectionUtils.isEmpty(tableMetaData.getColumnMetaDatas())) {
                metaDatas.add(tableMetaData);
            }
        }
    }


    /**
     * 获取sql中涉及到的所有表和字段
     *
     * @return
     */
    private Map<String, List<String>> parserAllColumns(SchemaStatVisitor visitor) {
        Collection<TableStat.Column> columns = visitor.getColumns();
        return columns.stream().collect(Collectors.groupingBy(TableStat.Column::getTable, Collectors.mapping(TableStat.Column::getName, Collectors.toList())));
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

    /**
     * 获取子查询的字段和别名
     *
     * @return
     */
    private List<SQLSelectItem> parserSubqueryColumns(SQLStatement sqlStatement) {
        SQLSelect select = ((SQLSelectStatement) sqlStatement).getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        if (subQuerySource(query)) {
            SQLSubqueryTableSource subquery = (SQLSubqueryTableSource) query.getFrom();
            MySqlSelectQueryBlock block = (MySqlSelectQueryBlock) subquery.getSelect().getQuery();
            return block.getSelectList();
        }
        return new ArrayList<>(0);
    }


    private boolean subQuerySource(SQLSelectQueryBlock query) {
        return query.getFrom() instanceof SQLSubqueryTableSource || query.getWhere() instanceof SQLSubqueryTableSource;
    }

}
