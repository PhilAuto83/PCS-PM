package com.phildev.pcs;


import com.phildev.pcs.domain.User;
import com.phildev.pcs.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToUserListForAdminUser() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS - User list</title>")))
                .andExpect(view().name("user/list"));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToUserListForUserRole() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToAddUserPageForUserRole() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToUserAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<title>PCS - Add user</title>")))
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToUserUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/user/update/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>user</b>")))
                .andExpect(content().string(containsString("<title>PCS - Update User</title>")))
                .andExpect(content().string(containsString("<input type=\"text\" id=\"username\" placeholder=\"User Name\" class=\"col-4\" name=\"username\" value=\"usertest\">")))
                .andExpect(content().string(containsString("<input type=\"password\" id=\"password\" placeholder=\"Password\" class=\"col-4\" name=\"password\" value=\"\">")))
                .andExpect(view().name("user/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingUserWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","Joey Tester")
                        .param("username", "Tester83")
                        .param("password", "Test0000@")
                        .param("role","USER"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"));
        Assertions.assertEquals("Joey Tester", userRepository.findByUsername("Tester83").getFullName());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testInvalidPasswordWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","Joey Tester")
                        .param("username", "Tester88")
                        .param("password", "Test0000")
                        .param("role","USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, 1 uppercase letter, 1 special character between @$!%?&amp;</p>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testExistingFullNameWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","User")
                        .param("username", "us84")
                        .param("password", "Test0000@")
                        .param("role","USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">User already exists with username or fullname</p>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testExistingUserNameWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","User20")
                        .param("username", "usertest")
                        .param("password", "Test0000@")
                        .param("role","USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">User already exists with username or fullname</p>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testEmptyFieldsWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","")
                        .param("username", "")
                        .param("password", "")
                        .param("role","USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attribute("validationError", "Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, 1 uppercase letter, 1 special character between @$!%?&"))
                .andExpect(model().attribute("connectedUser", "user"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">FullName is mandatory</p>")))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Username is mandatory</p>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingUserWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","Delete User")
                        .param("username", "duser1")
                        .param("password", "Test555@")
                        .param("role","USER"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"));
        int userIdToDelete = userRepository.findAll().getLast().getId();
        mockMvc.perform(get("/user/delete/"+userIdToDelete).with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"));
        org.assertj.core.api.Assertions.assertThat(userRepository.findByUsername("duser1")).isNull();
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingUserWithAdminRole() throws Exception {
        mockMvc.perform(post("/user/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","Last Tester")
                        .param("username", "last83")
                        .param("password", "Test0000@")
                        .param("role","USER"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"));
        int userIdToUpdate = userRepository.findAll().getLast().getId();
        mockMvc.perform(post("/user/update/"+userIdToUpdate).with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName","Last Tester2")
                        .param("username", "last888")
                        .param("password", "Test0000@")
                        .param("role","ADMIN"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"));
        Optional<User> userUpdated = userRepository.findById(userIdToUpdate);
        userUpdated.ifPresent(user -> org.assertj.core.api.Assertions.assertThat(user.getFullName()).isEqualTo("Last Tester2"));
        userUpdated.ifPresent(user -> org.assertj.core.api.Assertions.assertThat(user.getRole()).isEqualTo("ADMIN"));
    }
}
