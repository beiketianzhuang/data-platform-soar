package com.bektz.dataplatformsoar.configs;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.JdbcResultResp;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import com.bektz.dataplatformsoar.service.SchemaService;
import com.bektz.dataplatformsoar.service.SoarService;
import com.bektz.dataplatformsoar.sqlparser.SqlParserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private SoarService soarService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private SqlParserService sqlParserService;
    @Autowired
    private ObjectMapper mapper;

    private static final String CONNECTION_HEART_BEAT_MESSAGE = "HeartBeat";
    private static final String EXECUTE_QUERY_SQL_MARK = ":::";
    private static final String PARSER_SQL_COLUMN_MARK = "===";

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (message.getPayloadLength() == 0 || message.getPayload().equals(CONNECTION_HEART_BEAT_MESSAGE)) {
            return;
        }
        try {
            boolean contains = contains(message.getPayload(), EXECUTE_QUERY_SQL_MARK);
            //sql执行
            if (contains) {
                String[] split = StringUtils.split(message.getPayload(), EXECUTE_QUERY_SQL_MARK);
                JdbcResultResp jdbcResultResp = schemaService.executeSql(SqlVerifyReq.builder().schema(split[0]).sql(split[1]).build());
                sendMessage(session, mapper.writeValueAsString(jdbcResultResp));

            } else if (contains(message.getPayload(), PARSER_SQL_COLUMN_MARK)) {
                String s = substringAfter(message.getPayload(), PARSER_SQL_COLUMN_MARK);
                Map<String, Object> map = sqlParserService.parserSql(s);
                sendMessage(session, mapper.writeValueAsString(map));
            } else {
                SqlVerifyReq sqlVerifyReq = SqlVerifyReq.builder().sql(message.getPayload()).build();
                SqlVerifyResp verify = soarService.verify(sqlVerifyReq);
                sendMessage(session, mapper.writeValueAsString(verify));
            }

        } catch (Exception e) {
            log.error("sql分析或执行sql失败 sql:{}", message.getPayload(), e);
        }
    }

    private void sendMessage(WebSocketSession webSocketSession, String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        webSocketSession.sendMessage(textMessage);
    }

}
