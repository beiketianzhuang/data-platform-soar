package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.JdbcResultResp;
import com.bektz.dataplatformsoar.resp.SchemaResp;
import com.bektz.dataplatformsoar.service.jdbc.DataSourceManager;
import com.bektz.dataplatformsoar.service.jdbc.JdbcTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.*;

@Service
public class SchemaService {

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void addSchema(SchemaReq schemaReq) {
        dataSourceManager.initDataSource(schemaReq);
    }

    public Map<String, List<String>> tablesAndSchemas(String schema) {
        Set<String> schemas = dataSourceManager.getDataSourceMap().keySet();
        Map<String, List<String>> items = new HashMap<>(2);
        if (StringUtils.isBlank(schema)) {
            items.put("tables", new ArrayList<>());
        }
        if (!StringUtils.isBlank(schema)) {
            items.put("tables", jdbcTemplate.allTablesInSchema(schema));
        }
        items.put("schemas", new ArrayList<>(schemas));
        return items;
    }

    public List<SchemaResp> getSchemaResps() {
        Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
        List<SchemaResp> schemaResps = new ArrayList<>(dataSourceMap.size());
        dataSourceMap.forEach((key, value) -> {
            Map<String, List<String>> tableMaps = new HashMap<>();
            List<String> tables = jdbcTemplate.allTablesInSchema(key);
            for (String table : tables) {
                List<String> columns = jdbcTemplate.allColumnInTable(key, table);
                tableMaps.put(table, columns);
            }
            schemaResps.add(SchemaResp.builder().schema(key).tables(tableMaps).build());
        });
        return schemaResps;
    }

    public SchemaResp getSchemaResp(String schema) {
        List<String> tables = jdbcTemplate.allTablesInSchema(schema);
        Map<String, List<String>> tableMaps = new HashMap<>();
        for (String table : tables) {
            List<String> columns = jdbcTemplate.allColumnInTable(schema, table);
            tableMaps.put(table, columns);
        }
        return SchemaResp.builder().schema(schema).tables(tableMaps).build();
    }


    public JdbcResultResp executeSql(SqlVerifyReq sqlVerifyReq) {
        Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
        DataSource dataSource = dataSourceMap.get(sqlVerifyReq.getSchema());

        //todo 对敏感字段做脱敏查询
        JdbcResultResp jdbcResultResp = jdbcTemplate.executeSql(dataSource, sqlVerifyReq.getSql());
        return jdbcResultResp;
    }
}
