package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SoarServiceTest {
    @Autowired
    private SoarService soarService;

    @Test
    void verify() {
        SqlVerifyResp verify = soarService.verify(SqlVerifyReq.builder().sql("CREATE TABLE `testfyl` (`id` int(11) unsigned NOT NULL AUTO_INCREMENT,`test` varchar(20) DEFAULT NULL,`dfa` varchar(20) DEFAULT NULL,`hhh` varchar(20) DEFAULT NULL,PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULTCHARSET=utf8mb4;").build());
        Assert.assertNotNull(verify);
    }

}