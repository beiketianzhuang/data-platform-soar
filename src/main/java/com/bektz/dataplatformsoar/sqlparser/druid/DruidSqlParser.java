package com.bektz.dataplatformsoar.sqlparser.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.bektz.dataplatformsoar.sqlparser.ColumnItem;
import com.bektz.dataplatformsoar.sqlparser.CommonFactory;
import com.bektz.dataplatformsoar.sqlparser.druid.visitor.DefineMySqlSchemaStatVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;

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
