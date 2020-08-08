package com.tkd.springsecurity.inmemoryuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class InMemoryUserApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void testAccessAdminPage_RedirectToLoginPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/welcome").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(mvcResult -> mvcResult.getResponse().getRedirectedUrl().contains("/login"));
    }

    @Test
    void testAdminLogin_AndThen_AccessWelcomePage_For_Admin_And_User() throws Exception {
        // Login
        MockHttpSession sesssion = ((MockHttpSession) mvc.perform(SecurityMockMvcRequestBuilders
                .formLogin("/login").user("admin").password("admin"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn().getRequest().getSession());

        // Access Admin Welcome Page
        mvc.perform(MockMvcRequestBuilders.get("/admin/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Hey!! Welcome to Admin Page"));

        // Access User Welcome Page
        mvc.perform(MockMvcRequestBuilders.get("/user/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Hey!! Welcome to User Page"));
    }

    @Test
    void testUserLogin_AndThen_AccessWelcomePage_For_Admin_And_User() throws Exception {
        // Login
        MockHttpSession sesssion = ((MockHttpSession) mvc.perform(SecurityMockMvcRequestBuilders
                .formLogin("/login").user("user").password("password"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn().getRequest().getSession());

        // Access User Welcome Page
        mvc.perform(MockMvcRequestBuilders.get("/user/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Hey!! Welcome to User Page"));

        // Access Admin Welcome Page
        mvc.perform(MockMvcRequestBuilders.get("/admin/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

}
