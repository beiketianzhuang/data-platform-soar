package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import com.bektz.dataplatformsoar.service.SoarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SoarController {
    @Autowired
    private SoarService soarService;

    @ResponseBody
    @PostMapping(value = "/soar/sql::verify")
    public SqlVerifyResp verify(@RequestBody SqlVerifyReq sqlVerifyReq) {
        return soarService.verify(sqlVerifyReq);
    }

}
