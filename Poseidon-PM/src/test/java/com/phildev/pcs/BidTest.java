package com.phildev.pcs;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.repositories.BidListRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class BidTest {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListSaveTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);

		// Save
		bid = bidListRepository.save(bid);
		Assertions.assertEquals(bid.getBidQuantity(), 10d, 10d);
	}

	@Test
	public void bidListUpdateTest() {
		BidList bid2 = new BidList("Account Test2", "Type Test2", 10d);
		// Update
		bid2.setBidQuantity(20d);
		bid2 = bidListRepository.save(bid2);
		Assertions.assertEquals(bid2.getBidQuantity(), 20d, 20d);
	}

	@Test
	public void bidListSearchTest() {
		List<BidList> listResult = bidListRepository.findAll();
		Assertions.assertFalse(listResult.isEmpty());
	}

	@Test
	public void bidListDeleteTest() {
		BidList bid3 = new BidList("Account Test3", "Type Test3", 10d);
		Integer id = bid3.getBidListId();
		bidListRepository.delete(bid3);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assertions.assertFalse(bidList.isPresent());
	}
}
