package com.bektz.dataplatformsoar.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping(value = "/page/{page}")
    public String verify(@PathVariable("page") String page) {
        return page;
    }
}
