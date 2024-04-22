package com.phildev.pcs.service;


import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.repositories.BidListRepository;
import com.phildev.pcs.repositories.RatingRepository;
import com.phildev.pcs.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.RuleNameController} and {@link RuleNameRepository}
 * All the methods in this service helps saving, deleting, retrieving rule name in database
 */
@Service
public class RuleNameService {

    private static final Logger logger = LoggerFactory.getLogger(RuleNameService.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * This method calls {@link RuleNameRepository#findAll()} method to find all rule name in database
     * @return a list of RuleName
     */
    public List<RuleName> findAll(){
        List<RuleName> allRules = ruleNameRepository.findAll();
        if(allRules.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allRules;
    }

    /**
     * This method checks if a {@link RuleName} exists in database
     * @param ruleName
     * @return true or false if rule name already exists in database
     */
    public boolean checkRuleNameExists(RuleName ruleName){
        return ruleNameRepository.existsRuleNameByName(ruleName.getName());
    }

    /**
     * This method checks if a rule name we are trying to update already exists in database
     * @param currentRuleName is a string representing the current rule name
     * @param updatedRuleName is a string representing the name we want to put instead
     * @return true or false
     */
    public boolean checkRuleNameIsUpdatedWithSameNameOrNonExistingOne(RuleName currentRuleName, RuleName updatedRuleName){
        if(currentRuleName != null && updatedRuleName.getName().equals(currentRuleName.getName())){
            return true;
        }else if(ruleNameRepository.existsRuleNameByName(updatedRuleName.getName())){
            return false;
        } else{
            return true;
        }
    }

    /**
     * This method saves a {@link RuleName} in database
     * @param ruleName with its properties {@link RuleName#name}, {@link RuleName#description},
     * {@link RuleName#json}, {@link RuleName#template}, {@link RuleName#sqlStr}and {@link RuleName#sqlPart} that will be saved in database
     * @return a rule name which was saved successfully in database
     */
    public RuleName save(RuleName ruleName){
        return ruleNameRepository.save(ruleName);
    }

    /**
     * This method takes the id of a rule name we want to find and the method {@link RuleNameRepository#findById(Object)} will retrieve the rule name or not owing to Optional type
     * @param id which is the unique rule name identifier
     * @return an optional of rule name so it can be empty or return something
     */
    public Optional<RuleName> findById(Integer id){
        return ruleNameRepository.findById(id);
    }

    /**
     * This method takes the id matching a rule name we want to delete and the method {@link RuleNameRepository#deleteById(Object)} will delete the rule name
     * @param id which is the unique rule name identifier
     * @return void
     */
    public void delete(Integer id){
        ruleNameRepository.deleteById(id);
    }
}
