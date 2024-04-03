package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
