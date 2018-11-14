package com.bektz.dataplatformsoar.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "/page/{page}")
    public String verify(@PathVariable("page") String page) {
        return page;
    }
}
