package com.phildev.pcs;

import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.service.RatingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RatingTest {

	@Autowired
	private RatingService ratingService;

	@Test
	public void ratingSaveTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Save
		Rating ratingInDB = ratingService.save(rating);
		Assertions.assertThat(ratingInDB.getId()).isNotNull();
		Assertions.assertThat(ratingInDB.getOrderNumber()).isEqualTo(10);
	}

	@Test
	public void ratingUpdateTest() {
		Rating rating2 = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		// Update
		rating2.setOrderNumber(20);
		Rating ratingInDB = ratingService.save(rating2);
		Assertions.assertThat(ratingInDB.getOrderNumber()).isEqualTo(20);
	}

	@Test
	public void ratingSearchTest() {
		Rating ratingSearch = new Rating("AAA+", "AA-", "AAA", 100);
		Rating ratingInDb = ratingService.save(ratingSearch);
		// Find
		List<Rating> listResult = ratingService.findAll();
		long ratingFound = listResult.stream().filter(rating -> rating.getOrderNumber()==100).count();
		Assertions.assertThat(listResult.size()).isGreaterThan(0);
		Assertions.assertThat(ratingFound).isEqualTo(1);
	}

	@Test
	public void ratingDeleteTest() {
		Rating rating3 = new Rating("BBB", "BB-", "AA-", 16);
		Rating ratingInDB = ratingService.save(rating3);
		// Delete
		ratingService.delete(ratingInDB.getId());
		Optional<Rating> ratingDeleted = ratingService.findById(ratingInDB.getId());
		Assertions.assertThat(ratingDeleted).isEqualTo(Optional.empty());
	}

}
