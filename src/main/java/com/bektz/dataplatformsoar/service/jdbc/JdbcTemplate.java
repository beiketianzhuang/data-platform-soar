package com.bektz.dataplatformsoar.service.jdbc;

import com.alibaba.druid.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JdbcTemplate {

    private static final String SHOW_TABLES = "show tables";
    private static final String SHOW_COLUMNS_TABLE = "";


    @Autowired
    private DataSourceManager dataSourceManager;


    public List<String> allColumnInTable(String schema, String table) {
        ResultSet result = null;
        List<String> columns = new ArrayList<>();
        try {
            Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
            DataSource dataSource = dataSourceMap.get(schema);
            result = executeSql(dataSource, String.format(SHOW_COLUMNS_TABLE, table));
            while (result.next()) {
                String column = result.getString(1);
                columns.add(column);
            }
        } catch (SQLException e) {
            log.warn("执行sql失败 schema:{} table:{} sql:{}", schema, table, SHOW_COLUMNS_TABLE, e);
        } finally {
            JdbcUtils.close(result);
        }
        return columns;

    }


    public List<String> allTablesInSchema(String schema) {
        List<String> tables = new ArrayList<>();
        ResultSet result = null;
        try {
            Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
            DataSource dataSource = dataSourceMap.get(schema);
            result = executeSql(dataSource, SHOW_TABLES);
            while (result.next()) {
                String table = result.getString(1);
                tables.add(table);
            }
        } catch (SQLException e) {

        } finally {
            JdbcUtils.close(result);
        }
        return tables;
    }


    public ResultSet executeSql(DataSource dataSource, String sql) {
        Connection con = null;
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            con = dataSource.getConnection();
            pre = con.prepareStatement(SHOW_TABLES);
            result = pre.executeQuery();
        } catch (SQLException e) {

        } finally {
            JdbcUtils.close(pre);
            JdbcUtils.close(con);
        }
        return result;
    }
}
