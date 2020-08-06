package com.tkd.springsecurity.inmemoryuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WelcomeController {
    @GetMapping(value = "/admin/welcome")
    public String adminGreeting() {
        return "Hey!! Welcome to Admin Page";
    }

    @GetMapping(value = "/user/welcome")
    public String userGreeting() {
        return "Hey!! Welcome to User Page";
    }
}
