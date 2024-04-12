package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.RuleName;
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

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model, Principal connectedUser) {
        RuleName ruleName = new RuleName();
        logger.info("Accessing rule name add page with user {}", connectedUser.getName());
        model.addAttribute("ruleName", ruleName);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
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

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        RuleName currentRuleName = ruleNameService.findById(id).orElse(null);
        if(result.hasErrors()){
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rule name {}", errors.toString());
            return "ruleName/add";
        }else if(!ruleNameService.checkRuleNameIsUpdatedWithSameNameOrNonExistingOne(currentRuleName,ruleName)) {
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

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));
        ruleNameService.delete(ruleName.getId());
        model.addAttribute("users", ruleNameService.findAll());
        return "redirect:/ruleName/list";
    }
}
