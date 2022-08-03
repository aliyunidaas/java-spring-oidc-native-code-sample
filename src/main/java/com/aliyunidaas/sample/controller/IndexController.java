package com.aliyunidaas.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/8 6:04 PM
 * @author: longqiuling
 **/
@Controller
public class IndexController {

    /**
     * Home page.
     *
     * @return a string indicating which view to display.
     */
    @RequestMapping(value = "/")
    private String index() {
        return "index";
    }
}
