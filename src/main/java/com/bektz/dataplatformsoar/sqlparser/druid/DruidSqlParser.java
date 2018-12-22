package com.bektz.dataplatformsoar.sqlparser.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.stat.TableStat;
import com.bektz.dataplatformsoar.sqlparser.ColumnItem;
import com.bektz.dataplatformsoar.sqlparser.CommonFactory;
import com.bektz.dataplatformsoar.sqlparser.druid.visitor.DefineMySqlSchemaStatVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;
import static java.util.stream.Collectors.*;

@Service
public class DruidSqlParser extends AbstractDruidSqlParSer {

    @Autowired
    private CommonFactory commonFactory;

    public Map<String, ColumnItem> parserSql(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, MYSQL);
        DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor = new DefineMySqlSchemaStatVisitor();
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(mySqlSchemaStatVisitor);
        return parserSql(mySqlSchemaStatVisitor, sqlStatement);
    }

    public Map<String, Set<String>> parserSqlAllColumns(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, MYSQL);
        DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor = new DefineMySqlSchemaStatVisitor();
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(mySqlSchemaStatVisitor);
        Collection<TableStat.Column> columns = mySqlSchemaStatVisitor.getColumns();
        return columns.stream().collect(groupingBy(TableStat.Column::getTable, mapping(TableStat.Column::getName, toSet())));
    }

    @Override
    public Map<String, ColumnItem> parserSql(DefineMySqlSchemaStatVisitor mySqlSchemaStatVisitor, SQLStatement sqlStatement) {
        AbstractDruidSqlParSer druidSqlParSer;
        List<SQLSelectItem> outermostColumns = parserOutermostColumns(sqlStatement);
        boolean hasAsterisk = outermostColumns.stream().anyMatch(item -> ALL_COLUMNS_MARK.equals(item.toString()));
        if (hasAsterisk) {
            druidSqlParSer = commonFactory.getBean("hasAsterisk");
        } else {
            druidSqlParSer = commonFactory.getBean("nonAsterisk");
        }
        return druidSqlParSer.parserSql(mySqlSchemaStatVisitor, sqlStatement);
    }
}
