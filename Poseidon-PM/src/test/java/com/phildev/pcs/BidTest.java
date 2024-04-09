package com.phildev.pcs;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.service.BidListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class BidTest {

	@Autowired
	private BidListService bidListService;

	@Test
	public void bidListSaveTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);
		// Save
		BidList bidSaved = bidListService.save(bid);
		Assertions.assertEquals(10d, bidSaved.getBidQuantity());
	}

	@Test
	public void bidListUpdateTest() {
		BidList bid2 = new BidList("Account Test2", "Type Test2", 10d);
		// Update
		bid2.setBidQuantity(20d);
		BidList bid2Saved = bidListService.save(bid2);
		Assertions.assertEquals(20d, bid2Saved.getBidQuantity());
	}

	@Test
	public void bidListSearchTest() {
		BidList bid2 = new BidList("Account Test2", "Type Test2", 10d);
		BidList bidSearch = bidListService.save(bid2);
		List<BidList> listResult = bidListService.findAll();
		long bidFound = listResult.stream().filter(bidList -> bidList.getBidListId().equals(bidSearch.getBidListId())).count();
		Assertions.assertFalse(listResult.isEmpty());
		Assertions.assertEquals(1, bidFound);
	}

	@Test
	public void bidListDeleteTest() {
		BidList bid3 = new BidList("Account Test3", "Type Test3", 10d);
		BidList bidInDB = bidListService.save(bid3);
		bidListService.delete(bidInDB.getBidListId());
		Optional<BidList> bidDeleted = bidListService.findById(bidInDB.getBidListId());
		Assertions.assertFalse(bidDeleted.isPresent());
	}
}
