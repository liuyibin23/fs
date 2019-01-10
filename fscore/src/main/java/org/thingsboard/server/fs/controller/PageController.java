package org.thingsboard.server.fs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/up1")
//    @RequestMapping(value = "/up1")
    public String up(){
        return "up"; //当浏览器输入/up1，会返回 /static/up.html的页面
    }

    @GetMapping("testview")
    public String testView(){
        return "test";
    }

}
