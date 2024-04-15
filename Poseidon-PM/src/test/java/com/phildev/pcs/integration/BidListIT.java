package com.phildev.pcs.integration;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.repositories.BidListRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
public class BidListIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToBidListForAdminUser() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS - Bid list</title>")))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToBidListForUserRole() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToAddBidPageForUserRole() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToBidAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<title>PCS - Add bid</title>")))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = "admin83", roles={"ADMIN"})
    public void testAccessToBidUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>admin83</b>")))
                .andExpect(content().string(containsString("<title>PCS - Update bid</title>")))
                .andExpect(view().name("bidList/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingBidListWithAdminRole() throws Exception {
        mockMvc.perform(post("/bidList/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","998877")
                        .param("type", "type bid")
                        .param("bidQuantity", "100.00"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"));
        List<BidList> bids = bidListRepository.findBidListByAccountAndType("998877", "type bid");
        for(BidList bid: bids){
            if(!bid.getBidQuantity().equals(100d)&& bid.getType().equals("type bid")){
                Assertions.fail("No bid saved");
            }
        }
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingBidListWithEmptyFieldsReturnsErrors() throws Exception {
        mockMvc.perform(post("/bidList/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","")
                        .param("type", "")
                        .param("bidQuantity", ""))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("bidList/add"))
                .andExpect(content().string(containsString("account cannot be null or empty")))
                .andExpect(content().string(containsString("type cannot be null or empty")))
                .andExpect(content().string(containsString("bid quantity cannot be null")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingBidListWithEmptyQuantityReturnsError() throws Exception {
        mockMvc.perform(post("/bidList/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","112255")
                        .param("type", "bid test")
                        .param("bidQuantity", ""))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("bidList/add"))
                .andExpect(content().string(containsString("bid quantity cannot be null")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingBidListWithNumberOverMaxQuantityReturnsError() throws Exception {
        mockMvc.perform(post("/bidList/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","112255")
                        .param("type", "bid test")
                        .param("bidQuantity", "1000000.10"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("bidList/add"))
                .andExpect(content().string(containsString("maximum bid quantity should be 1 000 000, only 2 digits allowed after decimal point")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingBidList4WithAdminRole() throws Exception {
        mockMvc.perform(post("/bidList/update/5").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","888888")
                        .param("type", "type test5")
                        .param("bidQuantity", "4000.20"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"));
        Optional<BidList> bidListUpdated = bidListRepository.findById(5);
        if(bidListUpdated.isEmpty()){
            Assertions.fail("Error :No bid found in database");
        }else{
            Assertions.assertThat(bidListUpdated.get().getBidQuantity()).isEqualTo(4000.20);
        }

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingBidList3WithAdminRole() throws Exception {
        mockMvc.perform(get("/bidList/delete/3").with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"));
        Optional<BidList> bidListDeleted = bidListRepository.findById(3);
        if(bidListDeleted.isPresent()){
            Assertions.fail("Error : Bid was not deleted");
        }
    }


}
