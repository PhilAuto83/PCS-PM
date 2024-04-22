package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.service.BidListService;
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


    /**
     * This method is displaying curvePoint/list view when calling endpoint /curvePoint/list
     * @param model which is sending user connected and curvePoints to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return curvePoint/list view
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model, Principal connectedUser)
    {
        List<CurvePoint> curvePoints = curvePointService.findAll();
        model.addAttribute("curvePoints",curvePoints);
        model.addAttribute("connectedUser",connectedUser.getName());
        logger.info("User {} is accessing curve point list page", connectedUser.getName());
        return "curvePoint/list";
    }

    /**
     * This method is displaying curvePoint/add view when calling endpoint /curvePoint/add
     * @param model which is sending user connected and  a single {@link CurvePoint} object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return curvePoint/add view
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(Model model, Principal connectedUser) {

        CurvePoint curvePoint = new CurvePoint();
        model.addAttribute("curvePoint", curvePoint);
        model.addAttribute("connectedUser",connectedUser.getName());
        logger.info("User {} accessing curve point add page", connectedUser.getName());
        return "curvePoint/add";
    }

    /**
     * This method is validating {@link CurvePoint} object and saving it in database through {@link CurvePointService#save(CurvePoint)}
     * @param curvePoint which is a {@link CurvePoint} object
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return curvePoint/add if there is an error or redirect to /curvePoint/list endpoint to display new curve point list with freshly added curve point
     */
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

    /**
     * This method is displaying curvePoint/update view with a bid retrieved by its id through {@link CurvePointService#findById(Integer)}
     * @param id which is the path variable to retrieve curve point by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return curvePoint/update view
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid curve point id "+id));
        model.addAttribute("connectedUser",connectedUser.getName());
        model.addAttribute("curvePoint", curvePoint);
        logger.info("User {} accessing page to update curve point with id {}", connectedUser.getName(), id);
        return "curvePoint/update";
    }

    /**
     * This method is used to save a curve point which has been updated and retrieved by its id
     * @param id which is the path variable to retrieve curve point by id
     * @param curvePoint which is the {@link CurvePoint} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the curvePoint/add view if there is an error or the curvePoint/list if the update is successful
     */
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

    /**
     * This method is deleting a curve point by its id by using the service {@link CurvePointService#delete(Integer)}
     * @param id which is the path variable to retrieve curve point by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the curvePoint/list view after calling /curvePoint/list endpoint
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid curve point id "+id));
        logger.info("User {} deleted curve point with id {} ", connectedUser.getName(), id);
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }
}
