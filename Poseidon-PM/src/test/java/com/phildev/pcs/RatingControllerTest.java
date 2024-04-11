package com.phildev.pcs;


import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.repositories.RatingRepository;
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
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToRatingListForAdminUser() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>PCS - Rating list</title>")))
                .andExpect(view().name("rating/list"));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToRatingListForUserRole() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void testForbiddenAccessToAddRatingPageForUserRole() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToRatingAddPageForAdminUser() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(model().attributeExists("rating"))
                .andExpect(content().string(containsString("<title>PCS - Add rating</title>")))
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testAccessToUserUpdatePageForAdminUser() throws Exception {
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("<div class=\"ms-auto\">Logged in user: <b>user</b>")))
                .andExpect(content().string(containsString("<title>PCS - Update rating</title>")))
                .andExpect(content().string(containsString("")))
                .andExpect(content().string(containsString("")))
                .andExpect(view().name("rating/update"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testCreatingUserWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","AAA")
                        .param("sandPRating", "A+")
                        .param("fitchRating", "BBB+")
                        .param("orderNumber","2"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testInvalidNotationForMoodysWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","QSD")
                        .param("sandPRating", "A+")
                        .param("fitchRating", "BBB+")
                        .param("orderNumber","2"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Rating must follow agency notation A, B or C like AA, AAA, AAA+ or AAA- for A</p>")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testExistingRatingErrorWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","AAA")
                        .param("sandPRating", "A+")
                        .param("fitchRating", "BBB+")
                        .param("orderNumber","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(content().string(containsString("Rating already exists with this order number")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testOrderNumber5NotMatchingTradeIdWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","AAA")
                        .param("sandPRating", "A+")
                        .param("fitchRating", "BBB+")
                        .param("orderNumber","5"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(content().string(containsString("You cannot add a rating with an order number not matching a trade")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testEmptyFieldsWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber","3"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attribute("connectedUser", "user"))
                .andExpect(content().string(containsString("Rating must follow agency notation A, B or C like AA, AAA, AAA+ or AAA- for A")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testDeletingUserWithAdminRole() throws Exception {
        mockMvc.perform(post("/rating/validate").with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","AAA")
                        .param("sandPRating", "A-")
                        .param("fitchRating", "A+")
                        .param("orderNumber","4"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"));
        int ratingIdToDelete = ratingRepository.findAll().getLast().getId();
        mockMvc.perform(get("/rating/delete/"+ratingIdToDelete).with((csrf())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void testUpdatingUserWithAdminRole() throws Exception {
        int userIdToUpdate = ratingRepository.findAll().getLast().getId();
        mockMvc.perform(post("/rating/update/"+userIdToUpdate).with((csrf()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating","CCC")
                        .param("sandPRating", "C-")
                        .param("fitchRating", "C+")
                        .param("orderNumber","3"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"));
        Optional<Rating> ratingUpdated = ratingRepository.findById(userIdToUpdate);
        if(ratingUpdated.isPresent()){
            Assertions.assertThat(ratingUpdated.get().getOrderNumber()).isEqualTo(3);
            Assertions.assertThat(ratingUpdated.get().getFitchRating()).isEqualTo("C+");
            Assertions.assertThat(ratingUpdated.get().getSandPRating()).isEqualTo("C-");
        }else{
            Assertions.fail("Rating with order number 3 was not updated");
        }


     }
}
