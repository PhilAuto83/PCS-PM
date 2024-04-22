package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.service.RatingService;
import com.phildev.pcs.service.RuleNameService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class RuleNameController {

    private static final Logger logger = LoggerFactory.getLogger(RuleNameController.class);

    @Autowired
    private RuleNameService ruleNameService;

    /**
     * This method is displaying ruleName/list view when calling endpoint /ruleName/list
     * @param model which is sending user connected and rule names to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return ruleName/list view
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model, Principal connectedUser)
    {
        model.addAttribute("connectedUser", connectedUser.getName());
        List<RuleName> rules = ruleNameService.findAll();
        model.addAttribute("rules", rules);
        if(rules.isEmpty()){
            model.addAttribute("emptyRule", "You have currently no rules");
            logger.info("Rule list is currently empty");
        }
        return "ruleName/list";
    }

    /**
     * This method is displaying ruleName/add view when calling endpoint /ruleName/add
     * @param model which is sending user connected and  a single {@link RuleName} object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return ruleName/add view
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model, Principal connectedUser) {
        RuleName ruleName = new RuleName();
        logger.info("Accessing rule name add page with user {}", connectedUser.getName());
        model.addAttribute("ruleName", ruleName);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "ruleName/add";
    }

    /**
     * This method is validating {@link RuleName} object and saving it in database through {@link RuleNameService#save(RuleName)}
     * @param ruleName which is a {@link RuleName} object
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return ruleName/add if there is an error or redirect to /ruleName/list endpoint to display new rule name list with freshly added rule
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        if(result.hasErrors()){
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rule name {}", errors.toString());
            return "ruleName/add";
        } else if(ruleNameService.checkRuleNameExists(ruleName)) {
            model.addAttribute("ruleExists", "Rule already exists with this name");
            logger.error("Rule already exists with name {}", ruleName.getName());
            return "ruleName/add";
        }
        RuleName ruleSaved = ruleNameService.save(ruleName);
        logger.info("Rule name {} was successfully saved", ruleSaved.getName());
        return "redirect:/ruleName/list";
    }

    /**
     * This method is displaying ruleName/update view with a rule retrieved by its id through {@link RuleNameService#findById(Integer)}
     * @param id which is the path variable to retrieve rule by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return ruleName/update view
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }


    /**
     * This method is used to save a rule which has been updated and retrieved by its id
     * @param id which is the path variable to retrieve rule by id
     * @param ruleName which is the {@link RuleName} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the ruleName/add view if there is an error or the ruleName/list if the update is successful
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model, Principal connectedUser) {
        RuleName currentRuleName = ruleNameService.findById(id).orElse(null);
        if(result.hasErrors()){
            model.addAttribute("connectedUser", connectedUser.getName());
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rule name {}", errors.toString());
            return "ruleName/add";
        }else if(!ruleNameService.checkRuleNameIsUpdatedWithSameNameOrNonExistingOne(currentRuleName,ruleName)) {
            model.addAttribute("connectedUser", connectedUser.getName());
            model.addAttribute("ruleExists", "Rule already exists with this name");
            logger.error("Rule already exists with name {}", ruleName.getName());
            return "ruleName/add";
        }else{
            ruleName.setId(id);
            RuleName ruleUpdated = ruleNameService.save(ruleName);
            logger.info("Rule with id {} and name {} updated successfully", ruleUpdated.getId(), ruleUpdated.getName());
            return "redirect:/ruleName/list";
        }

    }

    /**
     * This method is deleting a rule by its id by using the service {@link RuleNameService#delete(Integer)}
     * @param id which is the path variable to retrieve rule by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the ruleName/list view after calling /ruleName/list endpoint
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));
        ruleNameService.delete(ruleName.getId());
        model.addAttribute("users", ruleNameService.findAll());
        return "redirect:/ruleName/list";
    }
}
