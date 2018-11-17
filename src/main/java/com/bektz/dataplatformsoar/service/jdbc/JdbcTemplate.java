package com.bektz.dataplatformsoar.service.jdbc;

import com.alibaba.druid.util.JdbcUtils;
import com.bektz.dataplatformsoar.exception.BektzServerException;
import com.bektz.dataplatformsoar.resp.JdbcResultResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bektz.dataplatformsoar.constants.JdbcConstants.QUERY_FAILURE_CODE;
import static com.bektz.dataplatformsoar.constants.JdbcConstants.QUERY_FAILURE_MESSAGE;
import static com.bektz.dataplatformsoar.constants.QueryStatus.FAILURE;
import static com.bektz.dataplatformsoar.constants.QueryStatus.SUCCESS;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service()
public class JdbcTemplate {

    private static final String SHOW_TABLES = "show tables";
    private static final String SHOW_COLUMNS_TABLE = "select `table_name` as tableName, `column_key` as columnKey, " +
            "`column_name` as columnName, `column_type` as columnType, " +
            "`column_comment` as columnComment, `column_default` as columnDefault, `is_nullable` as isNullable " +
            "from information_schema.columns where `table_schema` = '%s' and `table_name` in ( '%s' );";
    private static final String SHOW_TABLE_FIELD = "Tables_in_%s";


    @Autowired
    private DataSourceManager dataSourceManager;


    public List<String> allColumnInTable(String schema, String table) {
        Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
        DataSource dataSource = dataSourceMap.get(schema);
        JdbcResultResp result = executeSql(dataSource, String.format(SHOW_COLUMNS_TABLE, schema, table));
        if (Objects.equals(result.getQueryStatus(), SUCCESS)) {
            return result.getResult()
                    .stream()
                    .map(map -> (String) map.get("columnName"))
                    .collect(toList());
        }
        throw new BektzServerException(QUERY_FAILURE_CODE, QUERY_FAILURE_MESSAGE);
    }

    public List<String> allTablesInSchema(String schema) {
        Map<String, DataSource> dataSourceMap = dataSourceManager.getDataSourceMap();
        DataSource dataSource = dataSourceMap.get(schema);
        JdbcResultResp result = executeSql(dataSource, SHOW_TABLES);
        if (Objects.equals(result.getQueryStatus(), SUCCESS)) {
            return result.getResult()
                    .stream()
                    .map(map -> (String) map.get(String.format(SHOW_TABLE_FIELD, schema)))
                    .collect(toList());
        }
        throw new BektzServerException(QUERY_FAILURE_CODE, QUERY_FAILURE_MESSAGE);
    }


    public JdbcResultResp executeSql(DataSource dataSource, String sql) {
        try {
            long startTime = System.currentTimeMillis();
            List<Map<String, Object>> result = JdbcUtils.executeQuery(dataSource, sql);
            long endTime = System.currentTimeMillis();
            return JdbcResultResp.builder().queryResultMsg(SUCCESS.value).queryStatus(SUCCESS).result(result).queryTimeInMs(endTime - startTime).build();
        } catch (SQLException e) {
            log.error("执行sql失败 sql:{}", sql, e);
            return JdbcResultResp.builder().queryResultMsg(FAILURE.value).queryStatus(FAILURE).queryErrorMsg(e.getMessage()).build();
        }
    }

}
