package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import com.bektz.dataplatformsoar.service.SoarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@Slf4j
public class BroadcastController {

    // 收到消息记数
//    private AtomicInteger count = new AtomicInteger(0);
    @Autowired
    private SoarService soarService;

    /**
     * @return
     * @MessageMapping 指定要接收消息的地址，类似@RequestMapping。除了注解到方法上，也可以注解到类上
     * @SendTo默认 消息将被发送到与传入消息相同的目的地
     * 消息的返回值是通过{@link org.springframework.messaging.converter.MessageConverter}进行转换
     */
    @MessageMapping("/receive")
    @SendTo("/topic/getResponse")
    public SqlVerifyResp broadcast(SqlVerifyReq sqlVerifyReq) {
        log.info("receive message = {}", sqlVerifyReq.getSql());
        return soarService.verify(sqlVerifyReq);
    }

    @RequestMapping(value = "/broadcast/index")
    public String broadcastIndex(HttpServletRequest req) {
        System.out.println(req.getRemoteHost());
        return "index";
    }

}