package com.bailiban.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Test {

    @RequestMapping(value = "get",method = RequestMethod.GET)
    public String get(User user){
        return user.toString();
    }




}
