package com.phildev.pcs;

import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.service.RuleNameService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@SpringBootTest
public class RuleTest {

	@Autowired
	private RuleNameService ruleNameService;

	@Test
	public void ruleSaveTest() {
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Save
		RuleName ruleInDB = ruleNameService.save(rule);
		Assertions.assertThat(ruleInDB.getId()).isNotNull();
		Assertions.assertThat(ruleInDB.getName()).isEqualTo("Rule Name");
	}

	@Test
	public void ruleUpdateTest() {
		RuleName rule2 = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Update
		rule2.setName("Rule Name Update");
		RuleName ruleInDB = ruleNameService.save(rule2);
		Assertions.assertThat(ruleInDB.getName()).isEqualTo("Rule Name Update");
	}

	@Test
	public void ruleSearchTest() {
		RuleName ruleSearch = new RuleName("Rule Name S", "Description S", "Json", "Template", "SQL", "SQL Part");
		ruleNameService.save(ruleSearch);
		// Find
		List<RuleName> listResult = ruleNameService.findAll();
		long ruleFound = listResult.stream().filter(ruleName -> Objects.equals(ruleName.getName(), "Rule Name S")).count();
		Assertions.assertThat(listResult.size()).isGreaterThan(0);
		Assertions.assertThat(ruleFound).isEqualTo(1);
	}

	@Test
	public void ruleDeleteTest() {
		RuleName rule3 = new RuleName("Rule Name3", "Description3", "Json", "Template3", "SQL", "SQL Part");
		// Delete
		RuleName ruleInDB = ruleNameService.save(rule3);
		ruleNameService.delete(ruleInDB.getId());
		Optional<RuleName> ruleDeleted = ruleNameService.findById(ruleInDB.getId());
		Assertions.assertThat(ruleDeleted).isEqualTo(Optional.empty());
	}
}
