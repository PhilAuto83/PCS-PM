package com.phildev.pcs.controllers;

import com.phildev.pcs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * This method is the login endpoint which displays the login view
     * User can enter credentials to be authenticated in the app
     * @return the login view
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     * This method is accessible to admin user and displays user list with their roles
     * @return a view user/list and send an object to the model which is the list of users
     */
    @GetMapping("/secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userService.findAll());
        mav.setViewName("user/list");
        return mav;
    }


}
