package com.tkd.springsecurity.basicauth.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class PrivateApiControllerTest extends BaseTest {

    private static final String PRIVATE_URL = "/private/welcome";

    @Test
    public void testWelcomeApiWithoutAuthorizationHeader() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(PRIVATE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testWelcomeApiWithAuthorizationHeader() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(PRIVATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(USER_NAME)
                        .password(PASSWORD)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
