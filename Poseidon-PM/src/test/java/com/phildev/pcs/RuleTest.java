package com.phildev.pcs;

import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.repositories.RuleNameRepository;
import com.phildev.pcs.service.RuleNameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RuleTest {

	@Autowired
	private RuleNameService ruleNameService;

	@Test
	public void ruleSaveTest() {
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Save
		RuleName ruleInDB = ruleNameService.save(rule);
		Assert.assertNotNull(ruleInDB.getId());
		Assert.assertTrue(ruleInDB.getName().equals("Rule Name"));
	}

	@Test
	public void ruleUpdateTest() {
		RuleName rule2 = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Update
		rule2.setName("Rule Name Update");
		RuleName ruleInDB = ruleNameService.save(rule2);
		Assert.assertTrue(ruleInDB.getName().equals("Rule Name Update"));
	}

	@Test
	public void ruleSearchTest() {
		RuleName ruleSearch = new RuleName("Rule Name S", "Description S", "Json", "Template", "SQL", "SQL Part");
		ruleNameService.save(ruleSearch);
		// Find
		List<RuleName> listResult = ruleNameService.findAll();
		Long ruleFound = listResult.stream().filter(ruleName -> Objects.equals(ruleName.getName(), "Rule Name S")).count();
		Assert.assertTrue(listResult.size() > 0);
		Assert.assertTrue(ruleFound==1);
	}

	@Test
	public void ruleDeleteTest() {
		RuleName rule3 = new RuleName("Rule Name3", "Description3", "Json", "Template3", "SQL", "SQL Part");
		// Delete
		RuleName ruleInDB = ruleNameService.save(rule3);
		ruleNameService.delete(ruleInDB.getId());
		Optional<RuleName> ruleDeleted = ruleNameService.findById(ruleInDB.getId());
		Assert.assertFalse(ruleDeleted.isPresent());
	}
}
