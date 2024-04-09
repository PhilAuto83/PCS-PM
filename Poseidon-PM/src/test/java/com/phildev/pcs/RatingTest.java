package com.phildev.pcs;

import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.service.RatingService;
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
public class RatingTest {

	@Autowired
	private RatingService ratingService;

	@Test
	public void ratingSaveTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Save
		Rating ratingInDB = ratingService.save(rating);
		Assert.assertNotNull(ratingInDB.getId());
		Assert.assertTrue(ratingInDB.getOrderNumber() == 10);
	}

	@Test
	public void ratingUpdateTest() {
		Rating rating2 = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Update
		rating2.setOrderNumber(20);
		Rating ratingInDB = ratingService.save(rating2);
		Assert.assertTrue(ratingInDB.getOrderNumber() == 20);
	}

	@Test
	public void ratingSearchTest() {
		Rating ratingSearch = new Rating("AAA+", "AA-", "AAA", 100);
		Rating ratingInDb = ratingService.save(ratingSearch);
		// Find
		List<Rating> listResult = ratingService.findAll();
		long ratingFound = listResult.stream().filter(rating -> rating.getOrderNumber()==100).count();
		Assert.assertTrue(listResult.size() > 0);
		Assert.assertTrue(ratingFound==1);
	}

	@Test
	public void ratingDeleteTest() {
		Rating rating3 = new Rating("BBB", "BB-", "AA-", 16);
		Rating ratingInDB = ratingService.save(rating3);
		// Delete
		ratingService.delete(ratingInDB.getId());
		Optional<Rating> ratingDeleted = ratingService.findById(ratingInDB.getId());
		Assert.assertFalse(ratingDeleted.isPresent());
	}

}
