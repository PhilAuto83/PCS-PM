package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.domain.User;
import com.phildev.pcs.service.CurvePointService;
import com.phildev.pcs.service.RatingService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class RatingController {

    private static Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;

    /**
     * This method is displaying rating/list view when calling endpoint /rating/list
     * @param model which is sending user connected and ratings to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return rating/list view
     */
    @GetMapping("/rating/list")
    public String home(Model model, Principal connectedUser)
    {
        List<Rating> ratings = ratingService.findAll();
        if(ratings == null || ratings.isEmpty()){
            model.addAttribute("emptyRatings", "You have currently no ratings");
        }
        model.addAttribute("ratings", ratings);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "rating/list";
    }

    /**
     * This method is displaying rating/add view when calling endpoint /rating/add
     * @param model which is sending user connected and  a single {@link Rating} object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return rating/add view
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model, Principal connectedUser) {
        Rating rating = new Rating();
        model.addAttribute("rating", rating);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "rating/add";
    }

    /**
     * This method is validating {@link Rating} object and saving it in database through {@link RatingService#save(Rating)}
     * @param rating which is a {@link Rating} object
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return rating/add if there is an error or redirect to /rating/list endpoint to display new rating list with freshly added rating
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        if(ratingService.checkRatingExists(rating)){
            model.addAttribute("ratingError", "Rating already exists with this order number");
            return "rating/add";
        }else if(!ratingService.checkOrderNumberExistInTrade(rating)){
            model.addAttribute("ratingError", "You cannot add a rating with an order number not matching a trade");
            return "rating/add";
        }else if(result.hasErrors()){
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rating {}", errors.toString());
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    /**
     * This method is displaying rating/update view with a rating retrieved by its id through {@link RatingService#findById(Integer)}
     * @param id which is the path variable to retrieve rating by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return rating/update view
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    /**
     * This method is used to save a rating which has been updated and retrieved by its id
     * @param id which is the path variable to retrieve rating by id
     * @param rating which is the {@link Rating} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the rating/add view if there is an error or the rating/list if the update is successful
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model, Principal connectedUser) {

        model.addAttribute("connectedUser", connectedUser.getName());
        if(result.hasErrors()){
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rating {}", errors.toString());
            return "rating/add";
        }
        rating.setId(id);
        ratingService.save(rating);
        logger.info("Rating with orderNumber {} has been updated", rating.getOrderNumber());
        return "redirect:/rating/list";
    }

    /**
     * This method is deleting a rating by its id by using the service {@link RatingService#delete(Integer)}
     * @param id which is the path variable to retrieve rating by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the rating/list view after calling /rating/list endpoint
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        ratingService.delete(rating.getId());
        model.addAttribute("users", ratingService.findAll());
        return "redirect:/rating/list";
    }
}
