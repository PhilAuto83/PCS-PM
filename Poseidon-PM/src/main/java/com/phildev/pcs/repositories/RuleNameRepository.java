package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {

    public boolean existsRuleNameByName(String name);

    public RuleName findRuleNameByName(String name);


}
