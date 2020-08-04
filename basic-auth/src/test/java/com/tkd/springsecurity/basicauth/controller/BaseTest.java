package com.tkd.springsecurity.basicauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class BaseTest {
    protected final String USER_NAME = "user"; // See the property value of spring.security.user.name
    protected final String PASSWORD = "password"; // See the property value of spring.security.user.password=

    @Autowired
    protected MockMvc mvc;
}
