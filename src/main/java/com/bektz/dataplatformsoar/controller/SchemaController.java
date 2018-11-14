package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller(value = "/schema")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @PostMapping
    public ResponseEntity<String> addSchema(@RequestBody SchemaReq schemaReq) {
        try {
            schemaService.addSchema(schemaReq);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加数据库失败");
        }
        return ResponseEntity.ok().body("添加成功");
    }
}
