package com.phildev.pcs;

import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.repositories.RuleNameRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RuleTest {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleSaveTest() {
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Save
		rule = ruleNameRepository.save(rule);
		Assert.assertNotNull(rule.getId());
		Assert.assertTrue(rule.getName().equals("Rule Name"));
	}

	@Test
	public void ruleUpdateTest() {
		RuleName rule2 = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Update
		rule2.setName("Rule Name Update");
		rule2 = ruleNameRepository.save(rule2);
		Assert.assertTrue(rule2.getName().equals("Rule Name Update"));
	}

	@Test
	public void ruleSearchTest() {
		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	public void ruleDeleteTest() {
		RuleName rule3 = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		// Delete
		Integer id = rule3.getId();
		ruleNameRepository.delete(rule3);
		Optional<RuleName> ruleList = ruleNameRepository.findById(id);
		Assert.assertFalse(ruleList.isPresent());
	}
}
