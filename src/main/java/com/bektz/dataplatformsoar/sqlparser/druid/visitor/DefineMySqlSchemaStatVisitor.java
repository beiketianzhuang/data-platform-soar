package com.bektz.dataplatformsoar.sqlparser.druid.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;

import java.util.ArrayList;
import java.util.List;

public class DefineMySqlSchemaStatVisitor extends MySqlSchemaStatVisitor {

    public List<SQLSelectItem> items = new ArrayList<>();


    @Override
    public boolean visit(SQLSelectQueryBlock x) {
        items.addAll(x.getSelectList());
        return super.visit(x);
    }

    @Override
    public TableStat.Column getColumn(SQLExpr expr) {
        return super.getColumn(expr);
    }
}
