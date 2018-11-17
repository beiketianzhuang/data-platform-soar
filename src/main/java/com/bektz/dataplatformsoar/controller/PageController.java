package com.bektz.dataplatformsoar.controller;


import com.bektz.dataplatformsoar.resp.SchemaResp;
import com.bektz.dataplatformsoar.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private SchemaService schemaService;

    @GetMapping(value = {"/index", "/"})
    public String index(Model model) {
        List<SchemaResp> schemaResps = schemaService.getSchemaResps();
        model.addAttribute("schemaResps", schemaResps);
        return "index";
    }


    @GetMapping(value = "/page/{page}")
    public String verify(@PathVariable("page") String page) {
        return page;
    }
}
