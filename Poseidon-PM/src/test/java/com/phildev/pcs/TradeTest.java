package com.phildev.pcs;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.repositories.TradeRepository;
import com.phildev.pcs.service.TradeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeTest {

	@Autowired
	private TradeService tradeService;

	@Test
	public void tradeSaveTest() {
		Trade trade = new Trade("Trade Account", "Type");
		// Save
		Trade tradeInDB = tradeService.save(trade);
		Assert.assertNotNull(tradeInDB.getTradeId());
		Assert.assertTrue(tradeInDB.getAccount().equals("Trade Account"));
	}

	@Test
	public void tradeUpdateTest() {
		Trade trade2 = new Trade("Trade Account2", "Type2");
		// Update
		trade2.setAccount("Trade Account Update");
		trade2 = tradeService.save(trade2);
		Assert.assertTrue(trade2.getAccount().equals("Trade Account Update"));
	}

	@Test
	public void tradeSearchTest() {
		Trade tradeSearch = new Trade("Trade Account S", "Type S");
		tradeService.save(tradeSearch);
		// Find
		List<Trade> listResult = tradeService.findAll();
		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	public void tradeDeleteTest() {
		Trade trade3 = new Trade("Trade Account3", "Type3");
		Trade tradeInDB = tradeService.save(trade3);
		// Delete
		tradeService.delete(tradeInDB.getTradeId());
		Optional<Trade> tradeDeleted = tradeService.findById(tradeInDB.getTradeId());
		Assert.assertFalse(tradeDeleted.isPresent());
	}

}
