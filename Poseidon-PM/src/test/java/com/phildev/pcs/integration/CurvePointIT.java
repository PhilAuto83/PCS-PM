package com.phildev.pcs.integration;


import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.repositories.CurvePointRepository;
import org.assertj.core.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CurvePointIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToCurvePointListForAdminUser() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(content().string(containsString("<title>PCS - Curve point list</title>")))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToCurvePointAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<title>PCS - Add curve point</title>")))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = "john83", roles={"ADMIN"})
    public void testAccessToCurvePointUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>john83</b>")))
                .andExpect(content().string(containsString("<title>PCS - Update curve point</title>")))
                .andExpect(content().string(containsString("<input type=\"number\" step=\"0.001\" id=\"term\" placeholder=\"Term\" class=\"col-4\" name=\"term\" value=\"150.005\">")))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingCurvePoint1WithAdminRole() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("term","500.505")
                        .param("value", "400.565"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"));
        Optional<CurvePoint> curvePointUpdated = curvePointRepository.findById(1);
        if(curvePointUpdated.isEmpty()){
            Assertions.fail("Error :No curve point found in database");
        }else{
            Assertions.assertThat(curvePointUpdated.get().getTerm()).isEqualTo(500.505);
            Assertions.assertThat(curvePointUpdated.get().getValue()).isEqualTo(400.565);
        }
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingCurvePointWithEmptyFieldsReturnsError() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("term","")
                        .param("value", ""))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(content().string(containsString("term cannot be null")))
                .andExpect(content().string(containsString("value cannot be null")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingCurvePointWithInvalidMinValueReturnsError() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("term","0.999")
                        .param("value", "0.1"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(content().string(containsString("minimum value must 1.000")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingCurvePointWithValidInputs() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("term","1.001")
                        .param("value", "1.005"))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/curvePoint/list"));
        CurvePoint curvePointSaved = curvePointRepository.findAll().getLast();
        Assertions.assertThat(curvePointSaved.getTerm()).isEqualTo(1.001);
        Assertions.assertThat(curvePointSaved.getValue()).isEqualTo(1.005);
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingCurvePointWithAdminRole() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/2").with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"));
        Optional<CurvePoint> curvePointDeleted = curvePointRepository.findById(2);
        if(curvePointDeleted.isPresent()){
            Assertions.fail("Error : Bid was not deleted");
        }
    }
}
