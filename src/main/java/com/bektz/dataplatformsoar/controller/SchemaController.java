package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.resp.GenericResponse;
import com.bektz.dataplatformsoar.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;


@Controller
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @PostMapping(value = "/schema")
    public ResponseEntity<GenericResponse> addSchema(@RequestBody @Valid SchemaReq schemaReq, BindingResult result) {
        if (result != null && result.getErrorCount() > 0) {
            List<ObjectError> allErrors = result.getAllErrors();
            StringBuilder sb = new StringBuilder();
            for (ObjectError allError : allErrors) {
                sb.append(allError.getDefaultMessage()).append(";");
            }
            return ResponseEntity.badRequest().body(GenericResponse.CLIENT_ERROR.setMessage(sb.toString()));
        }
        try {
            schemaService.addSchema(schemaReq);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.CLIENT_ERROR);
        }
        return ResponseEntity.ok(GenericResponse.SUCCESS);
    }
}
