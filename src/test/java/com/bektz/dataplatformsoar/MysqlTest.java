package com.bektz.dataplatformsoar;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MysqlTest {


    @Test
    public void mysqlTest () throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/local");
        dataSource.setPassword("root");
        dataSource.setMaxActive(2);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        DruidPooledConnection connection = dataSource.getConnection(1000);
        boolean asyncInit = dataSource.isAsyncInit();
        System.out.println(asyncInit);
//        DruidPooledConnection connection = dataSource.getConnection();
//        Statement statement = connection.createStatement();
//        boolean execute = statement.execute("select 1");
//        System.out.println(execute);
    }

}
