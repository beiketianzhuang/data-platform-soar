package com.bektz.dataplatformsoar;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.bektz.dataplatformsoar.sqlparser.ExportTableAliasVisitor;
import com.bektz.dataplatformsoar.sqlparser.SqlAnalyzer;
import lombok.Builder;
import lombok.Data;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class SqlParserTest {

    private Queue queue1 = new LinkedBlockingQueue();
    private Queue queue2 = new LinkedBlockingQueue();

    private static final String ALIAS_COLUMN = " AS %s";

    @Test
    public void testSelectTables() throws Exception {
        String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2 as a, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 " +
                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select selectStatement = (Select) statement;
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List tables = tablesNamesFinder.getTableList(selectStatement);
            for (Object table : tables) {
                System.out.println(table);
            }
        }
    }


    @Test
    public void testSelectColumns() throws Exception {
        String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 " +
                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
        sql = sql.replaceAll("`", "");
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select selectStatement = (Select) statement;
//            (PlainSelect)selectStatement.
            //获取最后需要查询的字段
            List<SelectItem> items = ((PlainSelect) selectStatement.getSelectBody()).getSelectItems();
            List<WithItem> withItemsList = selectStatement.getWithItemsList();
        }
    }

    @Test
    public void parserTableTest() throws Exception {
        String sql = "select a.* from (select * from alert inner join user on alert.id = user.id) a";

        parserTable(sql);
    }

    /**
     * 最简单的sql（select * from table）
     *
     * @param sql
     * @throws Exception
     */
    public void parserTable(String sql) throws Exception {
        //select * from alert select * from alert a    select * from alert as a    select a.name from alert a   select a.name n from alert a

        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select selectStatement = (Select) statement;

            //获取表名
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List tables = tablesNamesFinder.getTableList(selectStatement);
            for (Object table : tables) {
                System.out.println(table);
            }

//            (PlainSelect)selectStatement.
            //获取需要查询的字段
            List<SelectItem> items = ((PlainSelect) selectStatement.getSelectBody()).getSelectItems();
            if (items.size() == 1 && items.get(0) instanceof AllColumns) {
                //当查询为所有的字段 *

            } else {
                for (SelectItem item : items) {
                    SelectExpressionItem itemExp = (SelectExpressionItem) item;
                    Alias alias = itemExp.getAlias();
                    Expression expression = itemExp.getExpression();
                    queue1.add(expression.getASTNode().jjtGetValue());
                    queue2.add(alias.getName());
                }
            }

        }
    }


    @Test
    public void druidTest() {

//        String sql = "select * from laer";
//        String sql = "select a.name c from (select alert.name from alert inner join user on alert.id = user.id) a";
        String sql = "SELECT * ,username FROM MY_TABLE1, MY_TABLE2, (SELECT username FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 " +
                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
//            String sql = "select sname,st.sno,scn.cn from student st join (select distinct sno,count(*)cn from sc group by sc.sno)scn on(st.sno=scn.sno) where scn.cn=(select distinct count(cno) from course);\n";
//        String sql = "select a.na a  from (select u.name as na , u.age from user u inner join alert al on alert.id = user.id)  a";
//        String sql = "select a.m cc , a.nnn from (select c.mobile as m ,c.nnn nnn from card c where c.id = 1) a";
//        String sql = "select u.name,u.no  from (select u.name name ,c.card_no no from user u inner join card c on u.id = c.id) u";
//            String sql = "select * from (select a,avg(*) b from alert) c";
//            String sql = "select mobile,avg(mobile)  from card";
//        String sql = "select a.name c from alert a";
//        SQLExprParser exprParser = SQLParserUtils.createExprParser(sql, "mysql");
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        SchemaStatVisitor statVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);
        SQLStatement sqlStatement = sqlStatements.get(0);
        sqlStatement.accept(statVisitor);
        System.out.println(statVisitor.getColumns());
        Collection<TableStat.Column> columns = statVisitor.getColumns();
//        for (TableStat.Column column : columns) {
//            if (column.isSelect()) {
//                System.out.println(column.toString());
//            }
//        }
//        System.out.println(); // [t_user.name, t_user.age, t_user.id]
//        System.out.println(statVisitor.getTables()); // {t_user=Select}
//            System.out.println(statVisitor.getConditions());
        for (SQLStatement statement : sqlStatements) {
            SQLSelect select = ((SQLSelectStatement) statement).getSelect();
            SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
            System.out.println("最外层：" + query.getSelectList());
            if (query.getFrom() instanceof SQLSubqueryTableSource) {
                SQLSubqueryTableSource ssts = (SQLSubqueryTableSource) query.getFrom();
                MySqlSelectQueryBlock mssqb = (MySqlSelectQueryBlock) ssts.getSelect().getQuery();
                System.out.println(mssqb.getSelectList());//这里打印的就是子查询的*
            }

            if (query.getWhere() instanceof SQLSubqueryTableSource) {
                SQLSubqueryTableSource ssts = (SQLSubqueryTableSource) query.getFrom();
                MySqlSelectQueryBlock mssqb = (MySqlSelectQueryBlock) ssts.getSelect().getQuery();
                System.out.println(mssqb.getSelectList());//这里打印的就是子查询的*
            }
        }
        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer();
        Set<com.bektz.dataplatformsoar.sqlparser.TableMetaData> tableMetaData = sqlAnalyzer.parserRealMetaData(sql);
        System.out.println("最终:" + tableMetaData);

    }

    @Test
    public void visitorTest() throws Exception {
        final String dbType = JdbcConstants.MYSQL; // JdbcConstants.MYSQL或者JdbcConstants.POSTGRESQL
        String sql = "select a.name c from (select c.name from alert c inner join user u on c.id = u.id) a";
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        ExportTableAliasVisitor visitor = new ExportTableAliasVisitor();
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        SQLTableSource tableSource = visitor.getAliasMap().get("a");
//        System.out.println(tableSource);
//        DruidDataSourceFactory factory = new DruidDataSourceFactory();
//        DataSource dataSource = factory.createDataSource(new Properties());
//
//        Connection conn = dataSource.getConnection();
//        java.sql.Statement statement = conn.createStatement();
//        ResultSet rs = statement.executeQuery(sql);
////        获取返回结果的列名：
//        ResultSetMetaData m = rs.getMetaData();
    }


    @Test
    public void testSqlAnalyzer() throws JSQLParserException {
//        String sql = "select a.name c from (select c.name from alert c inner join user on c.id = user.id) a";
//        String sql = "select a.na dd  from (select u.name as na , u.age from user u inner join alert al on alert.id = user.id)  a";
        String sql = "select a.m cc from (select c.mobile as m  from card c) a";
//        String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 " +
//                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer();
//        Map<String, ColumnItem> stringColumnItemMap = sqlAnalyzer.parseSqlColumn(sql);
//        System.out.println(stringColumnItemMap);
    }

    @Test
    public void execMySqlTest() {
//        String sql = "select a.m cc from (select c.mobile as m  from card c) a";
        String sql = "select u.name n ,u.age from user u";
        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer();
        Set<com.bektz.dataplatformsoar.sqlparser.TableMetaData> tableMetaData = sqlAnalyzer.parserRealMetaData(sql);
        System.out.println(tableMetaData);
    }

    @Data
    @Builder
    static class TableMetaData {
        private String table;
        private String tableAlias;
        private List<ColumnMetaData> columnMetaDatas;
    }

    @Data
    @Builder
    static class ColumnMetaData {
        private String column;
        private String columnAlias;
    }


}
