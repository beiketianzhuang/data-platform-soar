package com.bektz.dataplatformsoar.configs;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import com.bektz.dataplatformsoar.service.SoarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private SoarService soarService;

    private static final String CONNECTION_HEART_BEAT_MESSAGE = "HeartBeat";

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (message.getPayloadLength() == 0 || message.getPayload().equals(CONNECTION_HEART_BEAT_MESSAGE)) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
//            SqlVerifyReq sqlVerifyReq = mapper.readValue(message.getPayload(), SqlVerifyReq.class);
            SqlVerifyReq sqlVerifyReq = SqlVerifyReq.builder().sql(message.getPayload()).build();
            SqlVerifyResp verify = soarService.verify(sqlVerifyReq);
            sendMessage(session, mapper.writeValueAsString(verify));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(WebSocketSession webSocketSession, String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        webSocketSession.sendMessage(textMessage);
    }

}
