package com.bektz.dataplatformsoar.controller;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.GenericResponse;
import com.bektz.dataplatformsoar.resp.JdbcResultResp;
import com.bektz.dataplatformsoar.resp.SchemaResp;
import com.bektz.dataplatformsoar.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @PostMapping(value = "/schemas")
    public ResponseEntity<GenericResponse> addSchema(@RequestBody @Valid SchemaReq schemaReq, BindingResult result) {
        schemaService.addSchema(schemaReq);
        return ResponseEntity.ok(GenericResponse.SUCCESS);
    }

    @GetMapping(value = "/schemas/{schema}")
    public String allTablesBySchema(@PathVariable("schema") String shcema, Model model) {
        SchemaResp schemaResp = schemaService.getSchemaResp(shcema);
        model.addAttribute("schemaResps", Collections.singletonList(schemaResp));
        return "index::table_refresh";
    }

    @GetMapping(value = "/schemas/all")
    public String allSchemas(Model model) {
        List<SchemaResp> schemaResps = schemaService.getSchemaResps();
        model.addAttribute("schemaResps", schemaResps);
        return "index::schema_refresh";
    }

    @PostMapping(value = "/schemas/tables/sql::execute")
    public JdbcResultResp executeSql(@RequestBody @Valid SqlVerifyReq sqlVerifyReq, BindingResult result) {
        return schemaService.executeSql(sqlVerifyReq);
    }

    @ResponseBody
    @GetMapping(value = "/schemas/tables/{schema}")
    public Map<String, List<String>> getablesBySchema(@PathVariable("schema") String schema) {
        return schemaService.tablesAndSchemas(schema);
    }

}
