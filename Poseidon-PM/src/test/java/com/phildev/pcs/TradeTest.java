package com.phildev.pcs;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class TradeTest {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeSaveTest() {
		Trade trade = new Trade("Trade Account", "Type");
		// Save
		trade = tradeRepository.save(trade);
		Assert.assertNotNull(trade.getTradeId());
		Assert.assertTrue(trade.getAccount().equals("Trade Account"));
	}

	@Test
	public void tradeUpdateTest() {
		Trade trade2 = new Trade("Trade Account", "Type");
		// Update
		trade2.setAccount("Trade Account Update");
		trade2 = tradeRepository.save(trade2);
		Assert.assertTrue(trade2.getAccount().equals("Trade Account Update"));
	}

	@Test
	public void tradeSearchTest() {
		// Find
		List<Trade> listResult = tradeRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	public void tradeDeleteTest() {
		Trade trade3 = new Trade("Trade Account", "Type");
		// Delete
		Integer id = trade3.getTradeId();
		tradeRepository.delete(trade3);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		Assert.assertFalse(tradeList.isPresent());
	}

}
