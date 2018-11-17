package com.bektz.dataplatformsoar.service.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.bektz.dataplatformsoar.exception.BektzClientException;
import com.bektz.dataplatformsoar.req.SchemaReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataSourceManager {

    private static final String MYSQL_CONNECTION_URL_TEMPLATE = "jdbc:mysql://%s:%s/information_schema?useUnicode=true&amp;characterEncoding=utf8";
    private ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap();

    public void initDataSource(SchemaReq schemaReq) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(schemaReq.getUsername());
        dataSource.setUrl(String.format(MYSQL_CONNECTION_URL_TEMPLATE, schemaReq.getAddress(), schemaReq.getPort()));
        dataSource.setPassword(schemaReq.getPassword());
        dataSource.setMaxActive(2);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        boolean asyncInit = dataSource.isAsyncInit();
        if (asyncInit) {
            dataSourceMap.put(schemaReq.getSchema(), dataSource);
        } else {
            log.warn("初始化数据库数据源失败 参数{} ", schemaReq);
            throw new BektzClientException(1001, "添加数据库失败");
        }
    }


}
