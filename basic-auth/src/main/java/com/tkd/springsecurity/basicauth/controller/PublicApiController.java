package com.tkd.springsecurity.basicauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class PublicApiController {
    @GetMapping(value = "/welcome")
    public String greeting() {
        return "Hey!! You have accessed public Api!";
    }

}
