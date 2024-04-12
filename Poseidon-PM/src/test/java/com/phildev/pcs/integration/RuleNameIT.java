package com.phildev.pcs.integration;


import com.phildev.pcs.repositories.RuleNameRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToRuleNameListForAdminUser() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS - Rule list</title>")))
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToRuleListForUserRole() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToAddRuleNamePageForUserRole() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToRuleNameAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<title>PCS - Add rule</title>")))
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToRuleNameUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>user</b>")))
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingRuleWithAdminRole() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 2")
                        .param("description", "this is rule 2")
                        .param("json", "json 2")
                        .param("template", "temp2")
                        .param("sqlStr", "select * from rulename where name='Rule 2';")
                        .param("sqlPart", "sql2"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"));
        Assertions.assertThat(ruleNameRepository.existsRuleNameByName("Rule 2")).isTrue();
        Assertions.assertThat(ruleNameRepository.findAll().getLast().getDescription()).isEqualTo("this is rule 2");
    }
    @ParameterizedTest(name="Test error message is {6} when passing name = {0}, description ={1}," +
            "json={2}, template={3}, sqlStr={4}, sqlPart={5}")
    @CsvSource({",this is rule 4, json4, temp4, sql4, sql4, name cannot be null or empty",
            "Rule 4, , json4, temp4, sql4, sql4, description cannot be null or empty",
            "Rule4, this is rule 4, ,temp4, sql4, sql4, json cannot be null or empty",
            "Rule4, this is rule4, json4,, sql4, sql4, template cannot be null or empty"})
    @WithMockUser(roles={"ADMIN"})
    public void testEmptyRuleNameWithAdminRole(String name, String description, String json, String template, String sqlStr, String sqlPart, String expectedMessage) throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", name)
                        .param("description", description)
                        .param("json", json)
                        .param("template", template)
                        .param("sqlStr", sqlStr)
                        .param("sqlPart", sqlPart))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testExistingRuleNameWithAdminRole() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 1")
                        .param("description", "This is existing rule 1")
                        .param("json", "json1")
                        .param("template", "template 1")
                        .param("sqlStr", "sqlStr")
                        .param("sqlPart", "sqlPart"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(content().string(containsString("Rule already exists with this name")));
    }



    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingRuleWithAdminRole() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 10")
                        .param("description", "This is rule 10")
                        .param("json", "json10")
                        .param("template", "template 10")
                        .param("sqlStr", "sqlStr10")
                        .param("sqlPart", "sqlPart10"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"));
        int ruleIdToDelete = ruleNameRepository.findRuleNameByName("Rule 10").getId();
        mockMvc.perform(get("/ruleName/delete/"+ruleIdToDelete).with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"));
        Assertions.assertThat(ruleNameRepository.existsRuleNameByName("Rule 10")).isFalse();
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingRuleWithAdminRole() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 7")
                        .param("description", "This is rule 7")
                        .param("json", "json7")
                        .param("template", "template 7")
                        .param("sqlStr", "sqlStr7")
                        .param("sqlPart", "sqlPart7"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"));
        int ruleIdToUpdate = ruleNameRepository.findRuleNameByName("Rule 7").getId();
        mockMvc.perform(post("/ruleName/update/"+ruleIdToUpdate).with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 8")
                        .param("description", "This is rule 7")
                        .param("json", "json7")
                        .param("template", "template 8")
                        .param("sqlStr", "sqlStr7")
                        .param("sqlPart", "sqlPart7"))
                .andExpect(view().name("redirect:/ruleName/list"));
        Assertions.assertThat(ruleNameRepository.existsRuleNameByName("Rule 8")).isTrue();
        Assertions.assertThat(ruleNameRepository.findAll().getLast().getTemplate()).isEqualTo("template 8");
        Assertions.assertThat(ruleNameRepository.findAll().getLast().getDescription()).isEqualTo("This is rule 7");
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingRuleNameWithExistingRuleNameWithAdminRole() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 18")
                        .param("description", "This is rule 18")
                        .param("json", "json18")
                        .param("template", "template 18")
                        .param("sqlStr", "sqlStr18")
                        .param("sqlPart", "sqlPart18"))
                .andExpect(view().name("redirect:/ruleName/list"));
        int ruleIdToUpdate = ruleNameRepository.findRuleNameByName("Rule 18").getId();
        mockMvc.perform(post("/ruleName/update/"+ruleIdToUpdate).with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Rule 1")
                        .param("description", "This is rule 17")
                        .param("json", "json17")
                        .param("template", "template 17")
                        .param("sqlStr", "sqlStr17")
                        .param("sqlPart", "sqlPart17"))
                .andExpect(status().is(200))
                .andExpect(view().name("ruleName/add"))
                .andExpect(content().string(containsString("Rule already exists with this name")));

    }

}
