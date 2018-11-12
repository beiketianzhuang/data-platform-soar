package com.bektz.dataplatformsoar.sqlparser;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Visitor（能够获取表和别名的关系）
 */
public class ExportTableAliasVisitor extends MySqlASTVisitorAdapter {

    private Map<String, SQLTableSource> aliasMap = new HashMap<>();

    @Override
    public boolean visit(SQLExprTableSource x) {
        String alias = x.getAlias();
        aliasMap.put(alias, x);
        return true;
    }

    public Map<String, SQLTableSource> getAliasMap() {
        return aliasMap;
    }
}
