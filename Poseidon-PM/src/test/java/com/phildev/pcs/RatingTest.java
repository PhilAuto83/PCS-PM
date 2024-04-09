package com.phildev.pcs;

import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.repositories.RatingRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RatingTest {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingSaveTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Save
		rating = ratingRepository.save(rating);
		Assert.assertNotNull(rating.getId());
		Assert.assertTrue(rating.getOrderNumber() == 10);
	}

	@Test
	public void ratingUpdateTest() {
		Rating rating2 = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Update
		rating2.setOrderNumber(20);
		rating2 = ratingRepository.save(rating2);
		Assert.assertTrue(rating2.getOrderNumber() == 20);
	}

	@Test
	public void ratingSearchTest() {
		// Find
		List<Rating> listResult = ratingRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	public void ratingDeleteTest() {
		Rating rating3 = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Delete
		Integer id = rating3.getId();
		ratingRepository.delete(rating3);
		Optional<Rating> ratingList = ratingRepository.findById(id);
		Assert.assertFalse(ratingList.isPresent());
	}

}
