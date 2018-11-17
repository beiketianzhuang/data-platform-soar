package com.bektz.dataplatformsoar.service.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.bektz.dataplatformsoar.exception.BektzClientException;
import com.bektz.dataplatformsoar.req.SchemaReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataSourceManager {

    private static final String MYSQL_CONNECTION_URL_TEMPLATE = "jdbc:mysql://%s:%s/%s";
    private ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap();

    public void initDataSource(SchemaReq schemaReq) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(schemaReq.getUsername());
        dataSource.setUrl(String.format(MYSQL_CONNECTION_URL_TEMPLATE, schemaReq.getAddress(), schemaReq.getPort(), schemaReq.getSchema()));
        dataSource.setPassword(schemaReq.getPassword());
        dataSource.setMaxActive(2);
        dataSource.setTestOnBorrow(true);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        try (Connection ignored = dataSource.getConnection(1000)) {
            dataSourceMap.put(schemaReq.getSchema(), dataSource);
        } catch (Exception e) {
            log.warn("初始化数据库数据源失败 参数{} ", schemaReq);
            throw new BektzClientException(1001, "添加数据库失败");
        }

    }

    public Map<String, DataSource> getDataSourceMap() {
        return Collections.unmodifiableMap(dataSourceMap);
    }


}
