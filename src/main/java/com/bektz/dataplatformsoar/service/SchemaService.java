package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.resp.SchemaResp;
import com.bektz.dataplatformsoar.service.jdbc.DataSourceManager;
import com.bektz.dataplatformsoar.service.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemaService {

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void addSchema(SchemaReq schemaReq) {
        dataSourceManager.initDataSource(schemaReq);
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

}
