package com.phildev.pcs.service;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.repositories.CurvePointRepository;
import com.phildev.pcs.repositories.RatingRepository;
import com.phildev.pcs.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.RatingController} and {@link RatingRepository}
 * All the methods in this service helps saving, deleting, retrieving rating in database
 */
@Service
public class RatingService {

    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TradeRepository tradeRepository;

    /**
     * This method calls {@link RatingRepository#findAll()} method to find all ratings in database
     * @return a list of Rating
     */
    public List<Rating> findAll(){
        List<Rating> allRatings = ratingRepository.findAll();
        if(allRatings.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allRatings;
    }

    /**
     * This methods checks if a {@link com.phildev.pcs.domain.Trade#tradeId} exists with the {@link Rating#orderNumber} in database
     * @param rating
     * @return true or false if trade id exists
     */
    public boolean checkOrderNumberExistInTrade(Rating rating){
        // checking trade id matches orderNumber to add a rating
        return tradeRepository.existsById(rating.getOrderNumber());
    }

    /**
     * This methods checks if a rating already exists in database with the same agency ratings (moodys, fitch and S and P)
     * @param rating
     * @return true or false if rating exists
     */
    public boolean checkRatingExists(Rating rating){
        Optional<Rating> ratingFound = ratingRepository.findByOrderNumber(rating.getOrderNumber());
        return ratingFound.isPresent();
    }

    /**
     * This method saves a {@link Rating} in database
     * @param rating with its properties {@link Rating#moodysRating} , {@link Rating#fitchRating}, {@link Rating#sandPRating} and {@link Rating#orderNumber} that will be saved in database
     * @return a rating which was saved successfully in database
     */
    public Rating save(Rating rating){
        return ratingRepository.save(rating);
    }

    /**
     * This method takes the id of a rating we want to find and the method {@link RatingRepository#findById(Object)} will retrieve the rating or not owing to Optional type
     * @param id which is the unique rating identifier
     * @return an optional of rating so it can be empty or return something
     */
    public Optional<Rating> findById(Integer id){
        return ratingRepository.findById(id);
    }

    /**
     * This method takes the id of a rating we want to find and the method {@link RatingRepository#findById(Object)} will find the rating
     * @param id which is the unique rating identifier
     * @return an optional of Rating which can be empty or have a rating
     */
    public Optional<Rating> findByOrderNumber(Integer id){
        return ratingRepository.findByOrderNumber(id);
    }

    /**
     * This method takes the id matching a trade id we want to delete and the method {@link RatingRepository#deleteById(Object)} will delete the rating
     * @param id which is the unique rating identifier
     * @return void
     */
    public void delete(Integer id){
        ratingRepository.deleteById(id);
    }
}
