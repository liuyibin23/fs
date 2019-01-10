package org.thingsboard.server.fs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserInfoController {

    @RequestMapping(value = "/getusername",method = RequestMethod.GET)
    public String getUserName(HttpServletRequest request){
        return (String) request.getSession().getAttribute("username");
    }
}
