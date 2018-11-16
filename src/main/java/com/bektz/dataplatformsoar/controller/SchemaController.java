package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.resp.GenericResponse;
import com.bektz.dataplatformsoar.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@Controller
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @PostMapping(value = "/schema")
    public ResponseEntity<GenericResponse> addSchema(@RequestBody @Valid SchemaReq schemaReq, BindingResult result) {
        try {
            schemaService.addSchema(schemaReq);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.CLIENT_ERROR);
        }
        return ResponseEntity.ok(GenericResponse.SUCCESS);
    }
}
