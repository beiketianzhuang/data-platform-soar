package com.bektz.dataplatformsoar.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.bektz.dataplatformsoar.exception.SqlParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;

@Slf4j
public class SqlAnalyzer {
    private static final int INPUT_SQL_COUNT_MAX = 1;

    public List<TableMetaData> queryResult(String sql) {
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
    private List<TableMetaData> parserRealOutermostColumns(Map<String, List<String>> allColumns,
                                                           List<SQLSelectItem> outermostColumns,
                                                           List<SQLSelectItem> subqueryColumns) {
        List<TableMetaData> metaDatas = new ArrayList<>(outermostColumns.size());
        if (CollectionUtils.isEmpty(subqueryColumns)) {
            Set<String> tables = allColumns.keySet();
            tables.forEach(table -> {
                TableMetaData tableMetaData = new TableMetaData();
                List<ColumnMetaData> columnMetaDatas = tableMetaData.getColumnMetaDatas();
                allColumns.get(table).forEach(column -> outermostColumns.forEach(outer -> {
                    if (allColumns.get(table).contains(outer.getExpr().toString())) {
                        columnMetaDatas.add(ColumnMetaData.builder().column(column).columnAlias(outer.getAlias()).build());
                        tableMetaData.setTable(table);
                    }
                }));
                metaDatas.add(tableMetaData);
            });
        }

        if (!CollectionUtils.isEmpty(subqueryColumns)) {

        }
        /**
         * [user.id, card.id, user.name, card.card_no]
         [u.name, u.no]
         [u.name AS name, c.card_no AS no]
         */
        if (allColumns.size() == outermostColumns.size()) {
//            Set<String> tables = allColumns.keySet();
//            tables.forEach(table -> {
//
//
//
//
//
//                TableMetaData tableMetaData = new TableMetaData();
//                List<ColumnMetaData> columnMetaDatas = tableMetaData.getColumnMetaDatas();
//                allColumns.get(table).forEach(column -> {
//                    columnMetaDatas.add(ColumnMetaData.builder().column(column.getName()).columnAlias("").build());
//                });
//
//            });
        }
        return null;
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
