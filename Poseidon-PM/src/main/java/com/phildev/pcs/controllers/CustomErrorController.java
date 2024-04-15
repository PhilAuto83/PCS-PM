package com.phildev.pcs.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);


    @GetMapping("/error")
    public ModelAndView handleErrors(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null && Integer.parseInt(status.toString())== HttpStatus.FORBIDDEN.value()){

            mav.addObject("pageTitle", "Access denied");
            String errorMessage= "You are not authorized for the requested data.";
            mav.addObject("user", request.getRemoteUser());
            mav.addObject("errorMsg", errorMessage);
            logger.error("User is not authorized to view this page");
            mav.setViewName("403");
        }else if(status != null && Integer.parseInt(status.toString())== HttpStatus.NOT_FOUND.value()){
            String errorMessage= "The requested page does not exist";
            mav.addObject("pageTitle", "Resource not found");
            mav.addObject("errorMsg", errorMessage);
            logger.error("Page requested does not exist in PCS app");
            mav.setViewName("404");
        }else{
            String errorMessage= "You made a bad request";
            mav.addObject("pageTitle", "Unknown request");
            mav.addObject("errorMsg", errorMessage);
            logger.error("Invalid request sent");
            mav.setViewName("400");
        }
        return mav;

    }
}
