package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.service.CurvePointService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class CurveController {

    private static final Logger logger = LoggerFactory.getLogger(CurveController.class);

    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model, Principal connectedUser)
    {
        List<CurvePoint> curvePoints = curvePointService.findAll();
        model.addAttribute("curvePoints",curvePoints);
        model.addAttribute("connectedUser",connectedUser.getName());
        logger.info("User {} is accessing curve point list page", connectedUser.getName());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(Model model, Principal connectedUser) {

        CurvePoint curvePoint = new CurvePoint();
        model.addAttribute("curvePoint", curvePoint);
        model.addAttribute("connectedUser",connectedUser.getName());
        logger.info("User {} accessing curve point add page", connectedUser.getName());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            errors.forEach(fieldError -> logger.error("Field {} : {}",fieldError.getField(), fieldError.getDefaultMessage()));
            model.addAttribute("connectedUser", connectedUser.getName());
            return "curvePoint/add";
        }
        CurvePoint curvePointSaved = curvePointService.save(curvePoint);
        logger.info("New curve point saved with id {}", curvePointSaved.getId());
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid curve point id "+id));
        model.addAttribute("connectedUser",connectedUser.getName());
        model.addAttribute("curvePoint", curvePoint);
        logger.info("User {} accessing page to update curve point with id {}", connectedUser.getName(), id);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            errors.forEach(fieldError -> logger.error("Field {} : {}",fieldError.getField(), fieldError.getDefaultMessage()));
            model.addAttribute("connectedUser", connectedUser.getName());
            return "curvePoint/add";
        }
        curvePoint.setId(id);
        logger.info("Curve point updated for id {} with term {} and value {}", curvePoint.getId(), curvePoint.getTerm(), curvePoint.getValue());
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid curve point id "+id));
        logger.info("User {} deleted curve point with id {} ", connectedUser.getName(), id);
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }
}
