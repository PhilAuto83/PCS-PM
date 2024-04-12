package com.phildev.pcs.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginIT {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAdminUserLandsOnUserListPage() throws Exception {
        mockMvc.perform(formLogin("/login").user("admintest").password("Admin2024@"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    public void testLoginPageErrorWhenEmptyCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("").password(""))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("login?error"));
    }

    @Test
    public void testLoginPageErrorWhenInvalidCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("T").password("2024"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("login?error"));
    }

    @Test
    public void testUnknownUserLandsOnErrorPage() throws Exception {
        mockMvc.perform(formLogin("/login").user("adminFake").password("Admin2024@"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("login?error"));
    }

    @Test
    public void testLoginPageAccess() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS Login</title>")))
                .andExpect(content().string(containsString(" <div class=\"fs-3 fw-bold mb-3\">Login with Username and Password</div>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUserListRedirectionForAdminUser() throws Exception {
        mockMvc.perform(get("/secure/article-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(content().string(containsString("<td>admintest</td>")))
                .andExpect(content().string(containsString("<td>Administrator</td>")))
                .andExpect(content().string(containsString("<td style=\"width: 25%\">ADMIN</td>")))
                .andExpect(content().string(containsString("<td>usertest</td>")))
                .andExpect(content().string(containsString("<td>User</td>")))
                .andExpect(content().string(containsString("<td style=\"width: 25%\">USER</td>")));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testAccessDeniedRedirectionForUser() throws Exception {
        mockMvc.perform(get("/secure/article-details"))
                .andExpect(status().is(403));
    }


}
