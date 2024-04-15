package com.phildev.pcs.integration;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.domain.User;
import com.phildev.pcs.repositories.TradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;
    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToTradeListForAdminUser() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS - Trade list</title>")))
                .andExpect(view().name("trade/list"));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToTradeListForUserRole() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToAddTradePageForUserRole() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToTradeAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<title>PCS - Add trade</title>")))
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToTradeUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>user</b>")))
                .andExpect(content().string(containsString("<title>PCS - Update trade</title>")))
                .andExpect(view().name("trade/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingTradeWithAdminRole() throws Exception {
        mockMvc.perform(post("/trade/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","888888")
                        .param("type", "type test5")
                        .param("buyQuantity", "100.00"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"));
       List<Trade>trades = tradeRepository.findTradeByAccount("888888");
       for(Trade trade: trades){
           if(!trade.getBuyQuantity().equals(100d)&&trade.getType().equals("type test5")){
               Assertions.fail("No trade saved");
           }
       }
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testEmptyFieldsWhenCreatingTradeWithAdminRole() throws Exception {
        mockMvc.perform(post("/trade/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","")
                        .param("type", "")
                        .param("buyQuantity", "0"))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("Account is mandatory")))
                .andExpect(content().string(containsString("Type is mandatory")))
                .andExpect(content().string(containsString("minimum bid quantity must be 1.0")))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testExceedingMaxQuantityWhenCreatingTradeWithAdminRole() throws Exception {
        mockMvc.perform(post("/trade/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","")
                        .param("type", "")
                        .param("buyQuantity", "4000000"))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("Account is mandatory")))
                .andExpect(content().string(containsString("Type is mandatory")))
                .andExpect(content().string(containsString("maximum bid quantity should be 1 000 000")))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testNonExistingBidWhenCreatingTradeWithAdminRole() throws Exception {
        mockMvc.perform(post("/trade/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","888888")
                        .param("type", "type test5")
                        .param("buyQuantity", "2000.15"))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("No bid found for this account, type and quantity")))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingTradeWithUnknownBidWithAdminRole() throws Exception {
        mockMvc.perform(post("/trade/update/2").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","8899888")
                        .param("type", "type test5")
                        .param("buyQuantity", "2000.20"))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("No bid found for this account, type and quantity")))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingTradeWithAdminRole() throws Exception {
        mockMvc.perform(get("/trade/delete/5").with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"));
        Optional<Trade> tradeDeleted = tradeRepository.findById(5);
        if(tradeDeleted.isPresent()){
            Assertions.fail("Error : Trade was not deleted");
        }
    }
}
