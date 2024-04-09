package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    public Optional<Rating> findByOrderNumber(Integer orderNumber);

}
