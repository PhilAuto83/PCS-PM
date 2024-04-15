package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.domain.User;
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

    @GetMapping("/rating/add")
    public String addRatingForm(Model model, Principal connectedUser) {
        Rating rating = new Rating();
        model.addAttribute("rating", rating);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "rating/add";
    }

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

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        model.addAttribute("rating", rating);
        return "rating/update";
    }

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

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        ratingService.delete(rating.getId());
        model.addAttribute("users", ratingService.findAll());
        return "redirect:/rating/list";
    }
}
