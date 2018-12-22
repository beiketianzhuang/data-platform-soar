package com.bektz.dataplatformsoar.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.bektz.dataplatformsoar.sqlparser.druid.DruidSqlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;

@Slf4j
@Service
public class SqlParserService {

    @Autowired
    private DruidSqlParser druidSqlParser;

    public Map<String, Object> parserSql(String sql) {
        Map<String, Object> map = new HashMap<>();
        List<SQLStatement> stmtList;
        try {
            stmtList = SQLUtils.parseStatements(sql, MYSQL);
        } catch (ParserException e) {
            map.put("error", "请输入正确的sql");
            return map;
        }
        if (stmtList.size() > 1 || !(stmtList.get(0) instanceof SQLSelectStatement)) {
            map.put("error", "不好意思只支持单条的查询sql");
            return map;
        }
        //查询的字段
        Map<String, ColumnItem> columnItemMap = druidSqlParser.parserSql(sql);
        map.put("parserSql", columnItemMap.keySet());
        Map<String, Set<String>> allColumns = druidSqlParser.parserSqlAllColumns(sql);
        //sql中所有的字段和表
        map.put("allColumns", allColumns);
        return map;
    }
}
