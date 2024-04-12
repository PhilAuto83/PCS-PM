package com.phildev.pcs;

import com.phildev.pcs.domain.Trade;

import com.phildev.pcs.service.TradeService;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;


@SpringBootTest
public class TradeTest {

	@Autowired
	private TradeService tradeService;

	@Test
	public void tradeSaveTest() {
		Trade trade = new Trade("Trade Account", "Type",100);
		// Save
		Trade tradeInDB = tradeService.save(trade);
		Assertions.assertThat(tradeInDB.getTradeId()).isNotNull();
		Assertions.assertThat(tradeInDB.getAccount()).isEqualTo("Trade Account");
	}

	@Test
	public void tradeUpdateTest() {
		Trade trade2 = new Trade("Trade Account2", "Type2",10);
		// Update
		trade2.setAccount("Trade Account Update");
		trade2 = tradeService.save(trade2);
		Assertions.assertThat(trade2.getAccount()).isEqualTo("Trade Account Update");
	}

	@Test
	public void tradeSearchTest() {
		Trade tradeSearch = new Trade("Trade Account S", "Type S",20);
		tradeService.save(tradeSearch);
		// Find
		List<Trade> listResult = tradeService.findAll();
		Assertions.assertThat(listResult.size()).isGreaterThan(0);
	}

	@Test
	public void tradeDeleteTest() {
		Trade trade3 = new Trade("Trade Account3", "Type3",100);
		Trade tradeInDB = tradeService.save(trade3);
		// Delete
		tradeService.delete(tradeInDB.getTradeId());
		Optional<Trade> tradeDeleted = tradeService.findById(tradeInDB.getTradeId());
		Assertions.assertThat(tradeDeleted).isEqualTo(Optional.empty());
	}

}
