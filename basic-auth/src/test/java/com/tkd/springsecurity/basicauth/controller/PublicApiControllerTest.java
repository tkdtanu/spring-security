package com.tkd.springsecurity.basicauth.controller;

import com.tkd.springsecurity.basicauth.BasicAuthApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = BasicAuthApplication.class)
@AutoConfigureMockMvc
public class PublicApiControllerTest extends BaseTest {

    private static final String PUBLIC_URL = "/public/welcome";

    @Test
    public void testWelcomeApiWithoutAuthorizationHeader() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(PUBLIC_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWelcomeApiWithAuthorizationHeader() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(PUBLIC_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(USER_NAME)
                        .password(PASSWORD)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
