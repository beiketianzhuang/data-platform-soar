package com.bektz.dataplatformsoar;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.JdbcUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlTest {


    DruidDataSource dataSource = null;

    @Before
    public void setup() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/local");
        dataSource.setPassword("root");
        dataSource.setMaxActive(2);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        DruidPooledConnection connection = dataSource.getConnection(1000);
    }


    @Test
    public void mysqlTest() throws SQLException {
        boolean asyncInit = dataSource.isAsyncInit();
        System.out.println(asyncInit);
//        DruidPooledConnection connection = dataSource.getConnection();
//        Statement statement = connection.createStatement();
//        boolean execute = statement.execute("select 1");
//        System.out.println(execute);
    }


    @Test
    public void jdbcUtilsTest() throws Exception {
        List<Map<String, Object>> show_tables = JdbcUtils.executeQuery(dataSource, "select * from test");
        Assert.assertTrue(show_tables.size() > 0);
    }

}
