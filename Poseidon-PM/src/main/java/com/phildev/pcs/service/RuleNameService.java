package com.phildev.pcs.service;


import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    private static final Logger logger = LoggerFactory.getLogger(RuleNameService.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> findAll(){
        List<RuleName> allRules = ruleNameRepository.findAll();
        if(allRules.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allRules;
    }

    public RuleName save(RuleName ruleName){
        return ruleNameRepository.save(ruleName);
    }

    public Optional<RuleName> findById(Integer id){
        return ruleNameRepository.findById(id);
    }

    public void delete(Integer id){
        ruleNameRepository.deleteById(id);
    }
}