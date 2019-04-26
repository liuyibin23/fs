package org.thingsboard.server.fs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/foo")
public class FooController {

    @PreAuthorize("hasAuthority('API')")
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String fooTest1(){
        return "foo test1";
    }

    @RequestMapping(value = "test2", method = RequestMethod.GET)
    public String fooTest2(){
        return "foo test2";
    }

}
