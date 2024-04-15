package com.phildev.pcs.service;

import com.phildev.pcs.domain.Rating;
import com.phildev.pcs.repositories.RatingRepository;
import com.phildev.pcs.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TradeRepository tradeRepository;

    public List<Rating> findAll(){
        List<Rating> allRatings = ratingRepository.findAll();
        if(allRatings.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allRatings;
    }

    public boolean checkOrderNumberExistInTrade(Rating rating){
        // checking trade id matches orderNumber to add a rating
        return tradeRepository.existsById(rating.getOrderNumber());
    }

    public boolean checkRatingExists(Rating rating){
        Optional<Rating> ratingFound = ratingRepository.findByOrderNumber(rating.getOrderNumber());
        return ratingFound.isPresent();
    }

    public Rating save(Rating rating){
        return ratingRepository.save(rating);
    }

    public Optional<Rating> findById(Integer id){
        return ratingRepository.findById(id);
    }

    public Optional<Rating> findByOrderNumber(Integer id){
        return ratingRepository.findByOrderNumber(id);
    }

    public void delete(Integer id){
        ratingRepository.deleteById(id);
    }
}
