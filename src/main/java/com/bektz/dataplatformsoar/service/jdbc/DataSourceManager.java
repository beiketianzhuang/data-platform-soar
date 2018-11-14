package com.bektz.dataplatformsoar.service.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.bektz.dataplatformsoar.req.SchemaReq;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceManager {

    private static final String MYSQL_CONNECTION_URL_TEMPLATE = "jdbc:mysql://%s:%s/information_schema?useUnicode=true&amp;characterEncoding=utf8";
    private ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap();

    public void initDataSource(SchemaReq schemaReq) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(schemaReq.getUserName());
        dataSource.setUrl(String.format(MYSQL_CONNECTION_URL_TEMPLATE, schemaReq.getUrl(), schemaReq.getPort()));
        dataSource.setPassword(schemaReq.getPassword());
        dataSource.setMaxActive(2);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSourceMap.put(schemaReq.getSchema(), dataSource);
    }


}
