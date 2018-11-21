package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.repository.SecretColumnRepository;
import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.JdbcResultResp;
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

//    @Autowired
//    private SecretColumnRepository secretColumnRepository;


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


    public JdbcResultResp executeSql(SqlVerifyReq sqlVerifyReq) {
        Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
        DataSource dataSource = dataSourceMap.get(sqlVerifyReq.getSchema());

        //todo 对敏感字段做脱敏查询
//        DruidSqlParser druidSqlParser = new DruidSqlParser();
//        Set<ColumnItem> columnItems = druidSqlParser.parserRealMetaData(sqlVerifyReq.getSql());
//        Map<String, List<SecretColumn>> secretColumnMaps = new HashMap<>(columnItems.size());
//        Map<String,String> secretColumnAndAlias = new HashMap<>(columnItems.size());
//        for (ColumnItem columnItem : columnItems) {
//            List<SecretColumn> secretColumnList = secretColumnMaps.get(columnItem.getTableName());
//            if (CollectionUtils.isEmpty(secretColumnList)) {
//                List<SecretColumn> secretColumns = secretColumnRepository.getSecretColumnsByTableAndSchemaName(columnItem.getTableName(), sqlVerifyReq.getSchema());
//                secretColumnMaps.put(columnItem.getTableName(), secretColumns);
//            }
//            if (!CollectionUtils.isEmpty(secretColumnList)) {
//                secretColumnList.forEach(secretColumn -> {
//                    if (secretColumn.getColumn().equals(columnItem.getColumnName())) {
//
//                    }
//                });
//            }
//        }
        JdbcResultResp jdbcResultResp = jdbcTemplate.executeSql(dataSource, sqlVerifyReq.getSql());
        return jdbcResultResp;
    }
}
